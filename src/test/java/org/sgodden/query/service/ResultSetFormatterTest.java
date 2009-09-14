package org.sgodden.query.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.sgodden.query.DataType;
import org.sgodden.query.Query;
import org.sgodden.query.ResultSet;
import org.sgodden.query.ResultSetColumn;
import org.sgodden.query.ResultSetFormatter;
import org.sgodden.query.ResultSetRow;

public class ResultSetFormatterTest {
	
	public static void main(String[] args) {
		Query q = new Query();
		q.setObjectClassName("org.example.Order");
		q.addColumn("orderNumber");
		q.addColumn("customerName");
		q.addColumn("date");
		
		ResultSet rs = new ResultSet();
		rs.setQuery(q);
		
		List<ResultSetRow> rows = new ArrayList<ResultSetRow>();
		for (int i = 0; i < 2; i++) {
			rows.add(makeRow(new Object[]{"number " + i, "name " + i, new LocalDate()}));
		}
		
		rs.setCachedPageRows(rows);
		rs.setRowCount(2);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		new ResultSetFormatter().toXml(rs, out, true);
		
		System.out.println(out.toString());
		
	}
	
	private static ResultSetRow makeRow(Object[] values) {
		ResultSetRow row = new ResultSetRow();
		ResultSetColumn[] cols = new ResultSetColumn[values.length];
		int i = 0;
		for (Object value : values) {
			ResultSetColumn col = new ResultSetColumn();
			col.setDataType(DataType.STRING);
			col.setValue(value);
			cols[i] = col;
			i++;
		}
		row.setColumns(cols);
		return row;
	}

}
