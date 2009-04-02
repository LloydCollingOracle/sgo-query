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
 * Encapsulates a row in a result set.
 * <p/>
 * FIXME - this class currently combines the public interface required by both consumers and producers of result set objects.  This needs to be refactored.
 * @author Simon Godden
 * @see ResultSet
 */
@SuppressWarnings("serial")
public class ResultSetRow 
		implements Serializable {
	
	private String id;
	private ResultSetColumn[] columns;
	
	/**
	 * Constructs a new result set row.
	 */
	public ResultSetRow(){		
	}
	
	/**
	 * Returns the row identifier.
	 * @return the row identifier.
	 */
	public String getId() {
		return id;
	}
	/**
	 * Returns the row identifier.
	 * @param id the row identifier.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the columns of this row.
	 * @return the columns of this row.
	 */
	public ResultSetColumn[] getColumns() {
		return columns;
	}
	/**
	 * Returns the columns of this row.
	 * @param columns the columns of this row.
	 */
	public void setColumns(ResultSetColumn[] columns) {
		this.columns = columns;
	}
}
