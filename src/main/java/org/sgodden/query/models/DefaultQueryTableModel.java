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

import org.sgodden.query.Query;
import org.sgodden.query.QueryColumn;

/**
 * A default implementation of a query table model, which allows simple queries
 * to be run. <p/> If you wish to use advanced features such as aggregate
 * functions, then you will need to extend {@link AbstractQueryTableModel} and
 * make the query yourself.
 * @author goddens
 */
@SuppressWarnings("serial")
public class DefaultQueryTableModel extends AbstractQueryTableModel {

    private Query query;
    private Object[] columnIdentifiers;
    
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

}