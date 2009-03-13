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
 * Encapsulates a column in the result set.
 * <p/>
 * FIXME - this class currently combines the public interface required by both consumers and producers of result set objects.  This needs to be refactored.
 * @author sgodden
 *
 */
@SuppressWarnings("serial")
public class ResultSetColumn 
		implements Serializable {
	
	private DataType dataType;
	private Object value;
	
	/**
	 * Returns the data type of the column.
	 * @return the data type of the column.
	 */
	public DataType getDataType() {
		return dataType;
	}
	/**
	 * Sets the data type of the column.
	 * @param dataType the data type of the column.
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	/**
	 * Returns the value of the column.
	 * @return the value of the column.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * Sets the value of the column.
	 * @param value the value of the column.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return super.toString()+ "{DataType=" + dataType + ", Value=" + value + "}";
	}

}
