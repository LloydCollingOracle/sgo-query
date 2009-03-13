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

/**
 * A column in query output.
 * <p/>
 * Columns may have aggregated functions specified on them.
 * @author goddens
 *
 */
@SuppressWarnings("serial")
public class QueryColumn 
		implements Serializable {
	
	private String attributePath;
	private AggregateFunction aggregateFunction;
	
	/**
	 * Constructs a new query column.
	 */
	public QueryColumn(){}
	
	/**
	 * Constructs a new query column.
	 * @param attributePath the (potentially nested) path of the attribute to be returned in this column.
	 */
	public QueryColumn(String attributePath){
		this.attributePath = attributePath;
	}
	
	/**
	 * Constructs a new query column.
	 * @param attributePath the (potentially nested) path of the attribute to be returned in this column.
	 * @param aggregateFunction the aggregate function to be applied to this column.
	 */
	public QueryColumn(String attributePath, AggregateFunction aggregateFunction){
		this.attributePath = attributePath;
		this.aggregateFunction = aggregateFunction;
	}

	/**
	 * Returns the aggregate function to be applied to this column.
	 * @return the aggregate function.
	 */
	public AggregateFunction getAggregateFunction() {
		return aggregateFunction;
	}

	/**
	 * Sets the aggregate function to be applied to this column.
	 * @param aggregateFunction the aggregate function to be applied to this column.
	 */
	public void setAggregateFunction(AggregateFunction aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}

	/**
	 * Returns the (potentially nested) path of the attribute to be returned in this column.
	 * @return the attribute path.
	 */
	public String getAttributePath() {
		return attributePath;
	}

	/**
	 * Sets the (potentially nested) path of the attribute to be returned in this column.
	 * @param attributePath the (potentially nested) path of the attribute to be returned in this column.
	 */
	public void setAttributePath(String attributePath) {
		this.attributePath = attributePath;
	}
	
	

}
