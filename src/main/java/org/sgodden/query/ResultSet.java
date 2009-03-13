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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.query.service.QueryService;

/**
 * Encapsulates the results of running a query.  This implementation retrieves
 * results in pages, the size of which is determined by the value of the fetch
 * size set in the query.
 * <p/>
 * Only the current page is retained in memory.  This
 * allows for constant memory usage regardless of result set size, but at the
 * expense of running an extra query every time the cursor moves out of the
 * range of the current page.
 * <p/>
 * FIXME - this class currently combines the public interface required by both
 * consumers and producers of result set objects.  This needs to be refactored.
 * 
 * @author sgodden
 *
 */
@SuppressWarnings("serial")
public class ResultSet implements Serializable {
    /**
     * The log.
     */
    private static final transient Log log = LogFactory.getLog(ResultSet.class);
    /**
     * The result set rows for the currently cached page.
     */
    private List<ResultSetRow> cachedPageRows;
    /**
     * The page index of the currently cached page.
     */
	private int currentPageIndex = -1;
    /**
     * The query service used to fetch result pages.
     */
	private QueryService queryService;
    /**
     * Whether the query bailed out due to too many results.
     */
	private boolean queryBailedOut = false;
	/**
	 * The total number of rows matching the query criteria (regardless of
	 * the setting of {@link Query#setMaxRows(int)}).
	 */
	private int rowCount;
    /**
     * The query that caused this result set to be created.
     */
	private Query query;

    /**
     * Creates a new result set.
     */
	public ResultSet(){}

	/**
	 * Returns the number of rows currently cached.
	 * @return the number of rows currently cached.
	 */
    public int getCachedRowCount() {
        return cachedPageRows.size();
    }

	/**
	 * Returns the query that was used to produce this
	 * result set.
	 * 
	 * @return
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * Returns the total number of rows that matched the query.
	 * 
	 * @return the number of rows in this result set.
	 * @see #getTotalRowCount()
	 */
	public int getRowCount() {
		return rowCount;
	}
	
	/**
	 * Returns the cached result set rows.
	 * @return the cached result set rows.
	 */
	public List<ResultSetRow> getCachedPageRows(){
		return cachedPageRows;
	}
	
	/**
	 * Returns the row at the specified index,
     * causing a fetch of the appropriate page if necessary.
	 * @param rowIndex the index of the required row.
	 * @return the specified row.
	 * @throws IllegalArgumentException if the row index is out of range.
	 */
	public ResultSetRow getRow(int rowIndex){
		maybeGetPage(rowIndex);
		
		int pageRowIndex = rowIndex;
		if (query.getFetchSize() > 0){
			pageRowIndex = rowIndex % query.getFetchSize();	
		}
		
		return cachedPageRows.get(pageRowIndex);
	}
	
	/**
	 * Returns the (zero-indexed) index of the page containing the requested row.
	 * @param rowIndex the requested row index.
	 * @return the index of the page containing that row.
	 */
	private int getPageIndex(int rowIndex){
		int ret=0;
		if (query.getFetchSize() > 0){
			ret = rowIndex / query.getFetchSize();
			/*
			 * Because we are zero-indexing, we don't need to
			 * check for remainder and increment.
			 */
		}
		return ret;
	}

	/**
	 * Returns whether the query bailed out and did not return
	 * any rows due to there being too many rows in the result set.
	 * @return whether the query bailed out.
	 */
	public boolean getQueryBailedOut() {
		return queryBailedOut;
	}

	/**
	 * Retrieves the page containing the specified row index, if it is not
     * already with the current page.
	 * @param rowIndex the row index.
	 */
	private void maybeGetPage(int rowIndex){

		int requestedPageIndex = getPageIndex(rowIndex);

		if (currentPageIndex != requestedPageIndex){
			log.debug("Retrieving page " + (requestedPageIndex + 1));
			// We need to re-run a copy of the query to fetch the next block
			Query nextFetch = query.makeClone();
            /*
             * We already know exactly what we are doing, so no need to
             * specify bail out, or to calculate a row count.
             */
			nextFetch.setBailOutSize(0);
            nextFetch.setCalculateRowCount(false);
			/*
			 * The offset is the page index (zero-indexed) multiplied by the fetch size.
			 * i.e. if we wanted page 2 (the third page), and the fetch size was 100,
			 * then the offset would be 200.
			 */
			nextFetch.setRowOffset(requestedPageIndex * query.getFetchSize());

			ResultSet nextFetchResults = queryService.executeQuery(nextFetch);
			cachedPageRows = nextFetchResults.getCachedPageRows();
            currentPageIndex = requestedPageIndex;
		}
	}

	/**
	 * Sets the cached result set rows.
	 * @param rows the result set rows.
	 */
	public void setCachedPageRows(List<ResultSetRow> rows) {
        this.cachedPageRows = rows;
        // we must be on page 0 now
        this.currentPageIndex = 0;
	}

	/**
	 * Sets the query that was used to produce this
	 * result set.
	 * @param query
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * Sets whether the query bailed out and did not return
	 * any rows due to there being too many rows in the result set.
	 * <p/>
	 * FIXME - this should not be on the public interface.
	 * @param queryBailedOut whether the query bailed out.
	 */
	public void setQueryBailedOut(boolean queryBailedOut) {
		this.queryBailedOut = queryBailedOut;
	}
	
	/**
	 * Sets the query service that will be used to retrieve further pages
	 * of this result set.
	 * 
	 * @param queryService
	 */
	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	/**
	 * Sets the total number of rows in the result set.
	 * @param rowCount the total number of rows in the result set.
	 */
	public void setRowCount(int rowCount) {
        log.debug("Total rows: " + rowCount);
        this.rowCount = rowCount;
	}

}