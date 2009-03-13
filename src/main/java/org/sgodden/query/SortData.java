package org.sgodden.query;

import java.io.Serializable;


/**
 * Simple holder for sort data.
 * @author sgodden
 */
public class SortData implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 20080528L;

    /**
     * The column index on which to sort, 1-indexed.
     */
    private int columnIndex;

    /**
     * Whether to sort the data ascending (true) or descending (false).
     */
    private boolean ascending;

    /**
     * Constructs a new sort data.
     * @param columnIndex the column index.
     * @param sortOrder the sort order.
     */
    public SortData(int columnIndex, boolean ascending) {
        this.columnIndex = columnIndex;
        this.ascending = ascending;
    }

    /**
     * Returns the sort column index, one-indexed.
     * @return the sort column inded, one-indexed.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Returns whether the sort should be performed ascending or descending.
     * @return true to sort ascending, false to sort descending.
     */
    public boolean getAscending() {
        return ascending;
    }

}