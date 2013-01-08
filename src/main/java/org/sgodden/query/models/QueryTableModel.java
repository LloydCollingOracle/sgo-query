package org.sgodden.query.models;

import org.sgodden.query.Query;
import org.sgodden.query.Restriction;

/**
 * A table model built on an instance of {@link Query}.
 * @author Simon Godden
 */
public interface QueryTableModel extends SortableTableModel, GroupingTableModel {

    /**
     * Returns the object identifier for the specified row.
     * @param row the row, zero-indexed.
     * @return the object identifier for the object at the specified row.
     */
    public String getIdForRow(int row);

    /**
     * Replaces the existing restriction on the query with the supplied restriction
     * @param r
     */
    public void replaceQueryRestriction(Restriction r);
}