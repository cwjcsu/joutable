package com.joutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author atlas
 * @date 2012-12-5
 */
public class DefaultTable implements Table {
	private String title;
	private String[] headers;

	private List<Row> body = new ArrayList<Row>();

	public DefaultTable() {
	}
	
	public DefaultTable(String title, String... headers) {
		super();
		this.title = title;
		this.headers = headers;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public void addRow(Object[] row) {
		if (row.length > headers.length) {
			throw new IllegalArgumentException("row(" + row.length
					+ ") is longer than header(" + headers.length + ")");
		}
		body.add(new Row(row));
	}
	public void addRow(Object[] row,Comparator<?> cellComparator) {
		if (row.length > headers.length) {
			throw new IllegalArgumentException("row(" + row.length
					+ ") is longer than header(" + headers.length + ")");
		}
		body.add(new Row(row,cellComparator));
	}


	@Override
	public String[] getHeaders() {
		return headers;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public List<Row> getRows() {
		return Collections.unmodifiableList(this.body);
	}
}
