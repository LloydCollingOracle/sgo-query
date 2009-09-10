package org.sgodden.query;

/**
 * Interface to be implemeted by classes that render result set
 * values to strings.
 * @author sgodden
 *
 */
public interface ResultSetCellRenderer {
	
	/**
	 * Returns a string representation of the result set cell.
	 * @param rs the result set.
	 * @param value the value retrieved from the column and row co-ordinates of the result set.
	 * @param column the column number.
	 * @param row the row number.
	 * @return the string representation of the value.
	 */
	public String renderString(ResultSet rs, Object value, int column, int row);

}
