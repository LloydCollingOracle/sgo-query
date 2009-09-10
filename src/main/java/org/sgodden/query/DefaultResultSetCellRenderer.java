package org.sgodden.query;

/**
 * Very simple default implementation of the result set cell renderer
 * interface.
 * @author sgodden
 */
public class DefaultResultSetCellRenderer implements ResultSetCellRenderer {

	/*
	 * (non-Javadoc)
	 * @see org.sgodden.query.ResultSetCellRenderer#renderString(org.sgodden.query.ResultSet, java.lang.Object, int, int)
	 */
	public String renderString(ResultSet rs, Object value, int column, int row) {
		return String.valueOf(value);
	}

}
