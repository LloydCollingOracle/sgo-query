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
	 * Refreshes the model based on the passed criterion.
	 * @param criterion the criterion.
	 */
    public void refresh(Restriction criterion);
    /**
     * Refreshes the model based on the passed criterion
     * and sort data.
     * @param criterion the criterion.
     * @param sortData the sort data.
     */
    public void refresh(Restriction criterion,
            SortData[] sortData);
}
