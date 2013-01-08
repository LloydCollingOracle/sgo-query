package org.sgodden.query.models;

import org.sgodden.query.Restriction;
import org.sgodden.query.SortData;

/**
 * A query table model that may be refreshed using different
 * criteria.
 * @author Simon Godden
 */
public interface RefreshableQueryTableModel extends QueryTableModel {
    /**
     * Refreshes the model
     */
    public void refresh();
    /**
     * Refreshes the model based on the sort data.
     * @param sortData the sort data.
     */
    public void refresh(
            SortData... sortData);
}
