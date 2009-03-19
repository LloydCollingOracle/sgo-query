package org.sgodden.query.models;

import java.util.Map;

/**
 * An interface that denotes the capability of a table model to group by
 * columns. This is an extension of the SortableTableModel interface that allows
 * for the generation of count columns that include values from the distinct
 * values shown in the grouped column.
 * 
 * An assumption is made that the grouping column is always the first column
 * in the sorting sequence.
 * 
 * @author Lloyd Colling
 * 
 */
public interface GroupingTableModel extends SortableTableModel {

    /**
     * Informs the grouping table model whether it should be performing
     * the grouping.
     * @param dataIndex
     */
    public void doGrouping(boolean doGrouping);
    
    /**
     * Returns the count of the values for each unique value in the grouped
     * table. If the table is not grouping, this will return null.
     * @return
     */
    public Map<Object, Long> getGroupCounts();
}
