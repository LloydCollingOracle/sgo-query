package org.sgodden.query;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Very simple default implementation of the result set cell renderer
 * interface.
 * <p>
 * Dates and times are formatted in ISO format, as per XML spec.
 * </p>
 * @author sgodden
 */
public class DefaultResultSetCellRenderer implements ResultSetCellRenderer {
	
	private DateTimeFormatter dateFormat = ISODateTimeFormat.date();

	/*
	 * (non-Javadoc)
	 * @see org.sgodden.query.ResultSetCellRenderer#renderString(org.sgodden.query.ResultSet, java.lang.Object, int, int)
	 */
	public String renderString(ResultSet rs, Object value, int column, int row) {
		if (value instanceof LocalDate) {
			return dateFormat.print((LocalDate)value);
		}
		else {
			return String.valueOf(value);
		}
	}

}
