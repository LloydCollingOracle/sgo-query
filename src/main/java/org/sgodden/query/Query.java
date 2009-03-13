/* =================================================================
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#
# ================================================================= */
package org.sgodden.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Encapsulates a simple query.
 * <p/>
 * TODO - provide usage examples.
 * <p/>
 * FIXME - fetch size and max rows do not work in conjunction
 * <p/>
 * TODO - allow the caller to set the WHERE clause directly rather than
 * using the filter criteria, and probably remove the filter criteria 
 * altogether.
 * 
 * @author goddens
 *
 */
public class Query 
	implements Serializable, Cloneable {
	
	/**
     * Serial version UID.
     */
    private static final long serialVersionUID = 20080909L;
    
    private String objectClass;
	private List<QueryColumn> queryColumns = new ArrayList<QueryColumn>(); 
	private Restriction filterCriterion;
	private Locale locale;
	private int fetchSize = 0;
	private int maxRows = 0;
	private int rowOffset = 0;
	private int bailOutSize = 0;
	private boolean calculateRowCount = false;
	private SortData[] sortData;
	
	/**
	 * Constructs a new query.
	 */
	public Query(){
	    setLocale(Locale.getDefault());
	}

	/**
	 * Returns the maximum rows to retrieve.
	 * @return
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * Sets the maximum number of rows to retrieve.
	 * <p/>
	 * This is not the same as {@link #setFetchSize(int)}.
	 * 
	 * @param maxRows
	 * @see #setFetchSize(int)
	 */
	public Query setMaxRows(int maxRows) {
		this.maxRows = maxRows;
		return this;
	}

	/**
	 * @see #setRowOffset(int)
	 * @return the row offset.
	 */
	public int getRowOffset() {
		return rowOffset;
	}

	/**
	 * Sets the offset of the to the first row that you want from
	 * the results of the query.
	 * 
	 * @param rowOffset
	 */
	public Query setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
		return this;
	}

	/**
	 * @see #setFetchSize(int)
	 * @return the fetch size
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * Sets a fetch size for the returned {@link ResultSet}.
	 * <p/>
	 * If this is set, then the result set will retrieve rows in
	 * multiple fetches, with the specified size.
	 * 
	 * @param limit
	 */
	public Query setFetchSize(int limit) {
		this.fetchSize = limit;
		return this;
	}

	/**
	 * @see #setLocale(Locale)
	 * @return the locale.
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * Sets the desired locale for queries containing locale-dependent attributes.
	 * @param locale
	 */
	public Query setLocale(Locale locale) {
		this.locale = locale;
		return this;
	}
	
	/**
	 * @see #setObjectClassName(String)
	 * @return the class.
	 */
	public String getObjectClassName() {
		return objectClass;
	}
	/**
	 * Sets the name of the class against which the query should be run.
	 * @param className the class name.
	 */
	public Query setObjectClassName(String className) {
		this.objectClass = className;
		return this;
	}
	
	/**
	 * Adds a new column to the list desired in the result set.
	 * @param attributePath the (potentially nested) path to the attribute to be listed in the result set.
	 * @return the modified query object (<code>this</code>).
	 * @see #addColumn(String, AggregateFunction)
	 */
	public Query addColumn(String attributePath){
		queryColumns.add(new QueryColumn(attributePath));
		return this;
	}
	
	/**
	 * Adds a new column to the list desired in the result set.
	 * @param attributePath the (potentially nested) path to the attribute to be listed in the result set.
	 * @param aggregateFunction the aggregation function to apply to this column.
	 * @return the modified query object (<code>this</code>).
	 */
	public Query addColumn(String attributePath, AggregateFunction aggregateFunction){
		queryColumns.add(new QueryColumn(attributePath, aggregateFunction));
		return this;
	}
	
	/**
	 * Returns the columns of the query.
	 * @return the list of columns.
	 */
	public List<QueryColumn> getColumns(){
		return queryColumns;
	}
	
	/**
	 * Directly sets the columns of the query.
	 * @param columns
	 * @return the modified query object (<code>this</code>).
	 */
	public Query setColumns(List<QueryColumn> columns){
		this.queryColumns = columns;
		return this;
	}

	/**
	 * Returns the filter criterion applied to the query.
	 * @return the filter criterion.
	 */
	public Restriction getFilterCriterion(){
		return filterCriterion;
	}
	
	/**
	 * Sets the filter criterion to apply to this query.
	 * @param filterCriteria the filter criteria to apply.
	 * @return the modified query object (<code>this</code>).
	 */
	public Query setFilterCriterion(Restriction filterCriterion){
		this.filterCriterion = filterCriterion;
		return this;
	}
	
	/**
	 * Returns a deep clone of this object (that is, all nested criteria and
	 * column objects are also copied).
	 * @return the new clone.
	 */
	public Query makeClone(){
		try {
			return (Query)clone();
		} catch(Exception e){
			throw new Error(e);
		}
	}

	/**
	 * See {@link #setBailOutSize(int)}.
	 * @return
	 */
	public int getBailOutSize() {
		return bailOutSize;
	}

	/**
	 * Sets the number of rows at which, if exceeded, will
	 * cause the main query not be run at all, presumably due
	 * to fears that it will take too long.
	 * <p/>
	 * A separate query is run first to determine the number of rows
	 * in the result set, and that query should execute relatively quickly (as
	 * it is a simple select on the main entity).
	 * <p/>
	 * Note that this is quite a crude tool - the actual performance of a query
	 * is determined not only by the number of rows returned, but by various other
	 * factors including the nature of joins and aggregate functions used in
	 * selected columns.
	 * 
	 * @param bailOutSize
	 */
	public Query setBailOutSize(int bailOutSize) {
		this.bailOutSize = bailOutSize;
		return this;
	}

	/**
	 * See {@link #setCalculateRowCount(boolean)}.
	 * @return whether the row count should be calculated.
	 */
	public boolean getCalculateRowCount() {
		return calculateRowCount;
	}

	/**
	 * Sets whether the total row count should be calculated
	 * for a query which has a fetch size specified.
	 * <p/>
	 * This is potentially an expensive operation, so it defaults
	 * to false.
	 * @param calculateRowCount
	 * @return this query.
	 */
	public Query setCalculateRowCount(boolean calculateRowCount) {
		this.calculateRowCount = calculateRowCount;
		return this;
	}
	
	/**
	 * Sets the sort data for primary sorting.
	 * <p>
	 * When set, the query will be sorted by the specified column
	 * first, and then by all other columns in the order that
	 * they were added to the query.
	 * </p>
	 * @param sortData the sort data.
	 * @return this query.
	 */
	public Query setSortData(SortData sortData) {
	    if (sortData != null)
	        this.sortData = new SortData[] {sortData};
	    else
	        this.sortData = null;
	    return this;
	}
    
    /**
     * Sets the sort data for all sorting.
     * <p>
     * When set, the query will be sorted by the specified columns only.
     * </p>
     * @param sortData the sort data.
     * @return this query.
     */
    public Query setSortDatas(SortData[] sortData) {
        this.sortData = sortData;
        return this;
    }

	/**
	 * Returns the sort data specified for the query, 
	 * or <code>null</code> if none was specified.
	 * @return the sort data, or <code>null</code> if none was specified.
	 */
	public SortData[] getSortData() {
	    return sortData;
	}

}
