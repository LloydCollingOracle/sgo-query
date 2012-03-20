package org.sgodden.query.models;

/**
 * Methods for adding caching to a QueryTableModel to speed up subsequent lookups
 * 
 * @author bhowell-thomas
 */
public interface CachingQueryTableModel {

    /**
     * Downloads all the data if it hasn't already been, and caches it.
     * @return the Object required from the local cache
     */
    public Object getValueFromCacheAt(int colIndex, int rowIndex);
    
    /**
     * Clear the cache.  Important to call this when you are done with the cache 
     * to clear the memory used and prevent data corruption if this object is reused. 
     */
    public void invalidateCache();
    
    /**
     * @return the cache data array - cache[column][row]
     */
    public Object[][] getCache();
}
