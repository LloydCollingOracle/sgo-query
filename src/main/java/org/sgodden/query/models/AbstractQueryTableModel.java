/*
 * ================================================================= # This
 * library is free software; you can redistribute it and/or # modify it under
 * the terms of the GNU Lesser General Public # License as published by the Free
 * Software Foundation; either # version 2.1 of the License, or (at your option)
 * any later version. # # This library is distributed in the hope that it will
 * be useful, # but WITHOUT ANY WARRANTY; without even the implied warranty of #
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU # Lesser
 * General Public License for more details. # # You should have received a copy
 * of the GNU Lesser General Public # License along with this library; if not,
 * write to the Free Software # Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA # #
 * =================================================================
 */
package org.sgodden.query.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import nextapp.echo.app.table.AbstractTableModel;
import nextapp.echo.app.table.TableModel;

import org.sgodden.query.AggregateFunction;
import org.sgodden.query.AndRestriction;
import org.sgodden.query.Operator;
import org.sgodden.query.OrRestriction;
import org.sgodden.query.Query;
import org.sgodden.query.Restriction;
import org.sgodden.query.ResultSet;
import org.sgodden.query.ResultSetColumn;
import org.sgodden.query.ResultSetRow;
import org.sgodden.query.SimpleRestriction;
import org.sgodden.query.SortData;
import org.sgodden.query.service.QueryService;

import com.google.inject.Provider;

/**
 * Abstract implementation of the {@link QueryTableModel} interface.
 * @author goddens
 */
@SuppressWarnings("serial")
public abstract class AbstractQueryTableModel extends AbstractTableModel
        implements RefreshableQueryTableModel {

    /**
     * The query service which will actually execute the queries.
     */
    private Provider < QueryService > serviceProvider;

    /**
     * The filter criterion used on the last query refresh.
     */
    private Restriction criterion;

    /**
     * Sets the query service to be used to run the queries.
     * @param queryService the query service.
     */
    public void setQueryServiceProvider(Provider < QueryService > queryService) {
        this.serviceProvider = queryService;
    }

    /**
     * The list of listeners to be notified of a model change.
     */
    private List < ModelListener > listeners = new ArrayList < ModelListener >();

    /**
     * The result set returned from the query service.
     */
    private ResultSet rs;
    
    /**
     * Whether the query is grouping by the first column in the sorting sequence
     */
    private boolean isGrouping = false;
    
    /**
     * The counts of the distinct values in the grouped column
     */
    private Map<Object, Long> groupCounts = null;

    /**
     * Constructs a new abstract query table model.
     */
    public AbstractQueryTableModel() {
        super();
    }

    /**
     * Constructs a new abstract query table model, using the passed service to
     * execute the queries.
     * @param service the query service.
     */
    public AbstractQueryTableModel(Provider < QueryService > service) {
        this();
        this.serviceProvider = service;
    }

    /**
     * Informs the grouping table model whether it should be performing
     * the grouping.
     * @param dataIndex
     */
    public void doGrouping(boolean doGrouping) {
        this.isGrouping = doGrouping;
        if (this.rs != null)
            updateGroupCounts(getQuery());
    }
    
    /**
     * Returns the grouping table model whether it is performing the
     * grouping.
     * @return
     */
    public boolean isGrouping() {
        return isGrouping;
    }
    
    /**
     * Returns the count of the values for each unique value in the grouped
     * table. If the table is not grouping, this will return null.
     * @return
     */
    public Map<Object, Long> getGroupCounts() {
        if (!isGrouping)
            return null;
        
        if (groupCounts == null)
            updateGroupCounts(getQuery());
        return groupCounts;
    }

    /**
     * Adds a listener to be notified of model changes.
     * @param listener the listener to be added.
     */
    public void addModelListener(ModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified model listener.
     * @param listener the listener to be removed.
     */
    public void removeModelListener(ModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Internal method which actually performs model refresh.
     * @param query the query to (re)execute.
     */
    protected void doRefresh(Query query) {
        groupCounts = null;
        rs = getQueryService().executeQuery(query);
        if (isGrouping)
            updateGroupCounts(query);
        fireTableDataChanged();
    }

    /**
     * Returns the array of column identifiers.
     * @return the array of column identifiers.
     */
    public abstract Object[] getColumnIdentifiers();

    /**
     * Sets the column identifiers
     * @param columnIdentifiers
     */
    public abstract void setColumnIdentifiers(Object[] columnIdentifiers);
    
    /**
     * Returns the column name for the specified index, zero-indexed.
     * @param column the column index, zero-indexed.
     */
    @Override
    public String getColumnName(int column) {
        return getColumnIdentifiers()[column].toString();
    }

    /**
     * Returns the result set.
     * @return the result set.
     */
    protected ResultSet getResultSet() {
        if (rs == null) {
            groupCounts = null;
            rs = getQueryService().executeQuery(getQuery());
            if (isGrouping)
                updateGroupCounts(getQuery());
        }
        return rs;
    }

    /**
     * See {@link TableModel#getValueAt(int, int)}.
     * @see TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int colIndex, int rowIndex) {
        ResultSetRow row = getResultSet().getRow(rowIndex);
        return row.getColumns()[colIndex].getValue();
    }

    /**
     * See {@link org.sgodden.query.models.QueryTableModel#getIdForRow(int)}.
     * @see org.sgodden.query.models.QueryTableModel#getIdForRow(int)
     */
    public String getIdForRow(int row) {
        return getResultSet().getRow(row).getId();
    }

    /**
     * See {@link TableModel#getRowCount()}
     * @see TableModel#getRowCount()
     */
    public int getRowCount() {
        if (!getResultSet().getQueryBailedOut()) {
            return getResultSet().getRowCount();
        }
        else {
            return 0;
        }
    }

    /**
     * See {@link TableModel#getColumnCount()}.
     * @see TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return getColumnIdentifiers().length;
    }

    /**
     * Returns the total number of matches for the query.
     * @return the total number of matches for the query.
     */
    public int getQueryMatchCount() {
        return getResultSet().getCachedRowCount();
    }

    /**
     * Returns whether the query bailed out and did not retrieve any rows, due
     * to their being too many matches.
     * @return whether the query bailed out.
     */
    public boolean getQueryBailedOut() {
        return getResultSet().getQueryBailedOut();
    }
    
    private QueryService getQueryService() {
        if (serviceProvider == null) {
            throw new NullPointerException("QueryService is null - did you forget to set it?");
        }
        return serviceProvider.get();
    }

    /**
     * Refreshes the model based on the specified filter criterion, which may be
     * null to retrieve all rows.
     * <p>
     * XXX - changing filter criteria really means the model is changing, so
     * this method should be removed?
     * </p>
     * @param criterion the filter criteria to put in the query, or
     *            <code>null</code> to perform no filtering.
     * @param sortData the primary sort data to use, or <code>null</code> to
     *            specify no primary sort.
     */
    protected void refresh(Restriction criterion,
            SortData sortData) {
        if (sortData != null)
            this.refresh(criterion, new SortData[] {sortData});
        else
            this.refresh(criterion, new SortData[0]);
    }

    /**
     * Refreshes the model based on the specified filter criterion, which may be
     * null to retrieve all rows.
     * <p>
     * XXX - changing filter criteria really means the model is changing, so
     * this method should be removed?
     * </p>
     * @param criterion the filter criteria to put in the query, or
     *            <code>null</code> to perform no filtering.
     * @param sortData the sort data to use, or <code>null</code> to
     *            specify no sort.
     */
    public void refresh(Restriction criterion,
            SortData[] sortData) {
        Query query = getQuery();

        if (criterion != null) {
            query.setFilterCriterion(criterion);
        }
        else{
            query.setFilterCriterion(null);
        }

        if (sortData != null) {
            query.setSortDatas(sortData);
        }

        doRefresh(query);
    }

    /**
     * Refreshes the model based on the specified filter criteria, which may be
     * null to retrieve all rows.
     * @param filterCriteria the filter criteria to put in the query, or
     *            <code>null</code> to perform no filtering.
     */
    public void refresh(Restriction criterion) {
        refresh(criterion, (SortData)null);
    }

    /**
     * Returns the query.
     * @return the query to run.
     */
    protected abstract Query getQuery();

    /**
     * See
     * {@link org.sgodden.query.ui.mvc.models.SortableTableModel#sort(int, org.sgodden.ui.mvc.models.SortOrder)}
     * .
     * @see org.sgodden.query.ui.mvc.models.SortableTableModel#sort(int,
     *      org.sgodden.ui.mvc.models.SortOrder)
     */
    public void sort(int columnIndex, boolean ascending) {
        refresh(criterion, new SortData(columnIndex, ascending));
    }

    /**
     * See
     * {@link org.sgodden.query.ui.mvc.models.SortableTableModel#sort(int[], boolean[])}
     * .
     * @see org.sgodden.query.ui.mvc.models.SortableTableModel#sort(int[], boolean[])
     */
    public void sort(int[] columnIndices, boolean[] ascending) {
        SortData[] sDatas = new SortData[columnIndices.length];
        for (int i = 0; i < sDatas.length; i++) {
            sDatas[i] = new SortData(columnIndices[i], ascending[i]);
        }
        refresh(criterion, sDatas);
    }

    /**
     * See
     * {@link org.sgodden.query.ui.mvc.models.SortableTableModel#sort(String[], boolean[])}
     * .
     * @see org.sgodden.query.ui.mvc.models.SortableTableModel#sort(String[], boolean[])
     */
    public void sort(String[] columnNames, boolean[] ascending) {
        SortData[] sDatas = new SortData[columnNames.length];
        Object[] colIds = getColumnIdentifiers();
        for (int i = 0; i < sDatas.length; i++) {
            int columnIndex = -1;
            for (int j = 0; j < colIds.length && columnIndex == -1; j++) {
                if (colIds[j].equals(columnNames[i]))
                    columnIndex = j;
            }
            if (columnIndex >= 0)
                sDatas[i] = new SortData(columnIndex, ascending[i]);
            else
                throw new IllegalArgumentException("Unknown column " + columnNames[i]);
        }
        refresh(criterion, sDatas);
    }
    
    public void updateGroupCounts(Query query) {
        groupCounts = new HashMap<Object, Long>();
        
        SortData groupingCol = query.getSortData()[0];
        
        Restriction r = query.getFilterCriterion();
        Locale l = query.getLocale();
        String className = query.getObjectClassName();
        Set<Object> values = new HashSet<Object>();
        boolean hasNull = false;
        for (int i = 0; i < getRowCount(); i++) {
            Object value = getValueAt(groupingCol.getColumnIndex(), i);
            if (value == null)
                hasNull = true;
            else
                values.add(value);
        }
        String attributePath = query.getColumns().get(groupingCol.getColumnIndex()).getAttributePath();
        
        AndRestriction andR = new AndRestriction();
        andR.and(r);
        
        if (!hasNull) {
            SimpleRestriction sr = new SimpleRestriction(attributePath, Operator.IN, values.toArray());
            andR.and(sr);
        } else {
            SimpleRestriction sr = new SimpleRestriction(attributePath, Operator.IN, values.toArray());
            SimpleRestriction sr2 = new SimpleRestriction(attributePath, Operator.EQUALS, new Object[] {null});
            
            OrRestriction orR = new OrRestriction(sr, sr2);
            andR.and(orR);
        }
        
        Query q = new Query();
        q.setObjectClassName(className);
        q.setLocale(l);
        q.setSortData(new SortData(0, groupingCol.getAscending()));
        q.setFilterCriterion(andR);
        q.addColumn(attributePath);
        q.addColumn("*", AggregateFunction.COUNT);
        q.setCalculateRowCount(true);
        q.setIncludeId(false);
        
        ResultSet results = getQueryService().executeQuery(q);
        
        int rows = results.getRowCount();
        for (int i = 0; i < rows; i++) {
            ResultSetRow rsr = results.getRow(i);
            ResultSetColumn[] cols = rsr.getColumns();
            Object value = cols[0].getValue();
            Long count = ((Number)cols[1].getValue()).longValue();
            groupCounts.put(value, count);
        }
    }

}