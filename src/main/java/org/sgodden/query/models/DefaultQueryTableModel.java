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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.query.Query;
import org.sgodden.query.QueryColumn;
import org.sgodden.query.Restriction;
import org.sgodden.query.ResultSet;
import org.sgodden.query.ResultSetColumn;
import org.sgodden.query.ResultSetRow;

/**
 * A default implementation of a query table model, which allows simple queries
 * to be run. <p/> If you wish to use advanced features such as aggregate
 * functions, then you will need to extend {@link AbstractQueryTableModel} and
 * make the query yourself.
 * @author goddens
 */
@SuppressWarnings("serial")
public class DefaultQueryTableModel extends AbstractQueryTableModel implements CachingQueryTableModel{

    private Query query;
    private Object[] columnIdentifiers;
    
    private static final transient Log log = LogFactory.getLog(DefaultQueryTableModel.class);
    
    /**
     * @See getValueFromCacheAt
     * cache[column][row]
     */
    protected Object[][]cache;
        
    /**
     * Creates a new DefaultQueryTableModel instance with the specified
     * parameters.
     * @param entityClass the class for which instance values are to be
     *            retrieved.
     * @param columnIdentifiers the column identifiers.
     * @param attributePaths the attribute paths for each column.
     */
    public DefaultQueryTableModel(Query query) {
        this.query = query;
        if (query.getFilterCriterion()!=null) {
        	this.criterion = query.getFilterCriterion();
        }
        setSortData(query.getSortData());
    }
    
    public DefaultQueryTableModel(Query query, Restriction restriction) {
    	this.criterion = restriction;
        this.query = query;
        setSortData(query.getSortData());
    }

    protected final Query getQuery() {
        return query;
    }

    /**
     * This default implementation uses the query column
     * attribute paths as the column identifiers.
     */
    @Override
    public Object[] getColumnIdentifiers() {
        if (columnIdentifiers == null) {
            List < Object > list = new ArrayList < Object >();
            for (QueryColumn col : query.getColumns()) {
                list.add(col.getAttributePath());
            }
            columnIdentifiers = list.toArray();
        }
        return columnIdentifiers;
    }

    @Override
    public void setColumnIdentifiers(Object[] columnIdentifiers) {
        this.columnIdentifiers = columnIdentifiers;
    }

    /**
     * Downloads all the data if it hasn't already been, and cache it.
     * @return the Object required from the local cache
     */
    public Object getValueFromCacheAt(int colIndex, int rowIndex) {
	
	// do we need to download the database
	if (cache == null) {
	    
	    // make the cache the right size
	    cache = new Object[getColumnCount()][getRowCount()];
	    log.debug("Created cache sized[cols][rows ["+ getColumnCount() + "],[" + getRowCount() + "]"); 
	    
	    // fill it
	    ResultSet rs = getResultSet();
	    Query query = rs.getQuery();
	    // Allow max rows to be set for the purpose of list view download, whilst retaining fetch size setting.
	    int originalFetchSize = query.getFetchSize();
	    int originalMaxRows = query.getMaxRows();
	    query.setFetchSize(0);
	    query.setMaxRows(getRowCount());
	    	    
	    log.trace("next, running refresh");	    
	    doRefresh(rs.getQuery());

            // Ensure we have reference to up-to-date result set
	    rs = getResultSet();
	    log.debug("refresh run, fetch size is "+ query.getFetchSize() + " and max rows is " + query.getMaxRows());
	    
	    int rowCounter=0, colCounter;
	    for(ResultSetRow row : rs.getCachedPageRows()) {
		colCounter = 0;
		String debugOutput = "", COMMA="";
		for (ResultSetColumn col : row.getColumns()){
		    cache[colCounter][rowCounter] = col.getValue();
		    try {
			debugOutput += COMMA + String.valueOf(colCounter) + ":" + col.getValue().toString();
		    } catch (NullPointerException notInterested) {
		        debugOutput += COMMA + "NULL";
		    }
		    COMMA = ", ";
		    colCounter++;
		}
		
		log.trace("row = " + debugOutput);
		rowCounter++;
	    }
  	    // Restore original fetch size and max rows settings
	    query.setFetchSize(originalFetchSize);
	    query.setMaxRows(originalMaxRows);
	} else {
	    log.trace("getting value from cache");
	}
	
	// return the value
	try {
	    //log.trace("Returning col : "+ colIndex + ", row : " + rowIndex + " = " + cache[colIndex][rowIndex].toString());
	    return cache[colIndex][rowIndex];
	} catch(NullPointerException npe) {
	    //log.trace("Returning col : "+ colIndex + ", row : " + rowIndex + " = NULL");
	    return null;
	}
    }
    
    /**
     * Clear the cache.  Important to call this when you are done with the cache 
     * to clear the memory used and prevent data corruption if this object is reused. 
     * @See getValueFromCacheAt
     */
    public void invalidateCache() {
	cache = null;
    }
    
    /**
     * @return the cache data array - cache[column][row]
     */
    public Object[][] getCache() {
	return cache;
    }
}
