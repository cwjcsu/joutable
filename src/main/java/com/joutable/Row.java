package com.joutable;

import java.util.Comparator;

/**
 * 代码表格的一行数据
 * @author atlas
 * @date 2012-12-11
 */
public class Row implements Comparable<Row> {

	private Object[] data;

	private Comparator<Object> comparator = null;

	Row(Object[] row) {
		this.data = row;
	}

	Row(Object[] row, Comparator comparator) {
		this(row);
		this.comparator = comparator;
	}

	public int compareTo(Row oo) {
		Object[] dd = oo.data;
		for (int i = 0; i < data.length; i++) {
			Object t = data[i];
			if (i >= dd.length)
				return 1;
			Object o = dd[i];
			if (comparator != null) {
				return comparator.compare(t, o);
			}
			Comparable t1 = null;
			Comparable o1 = null;
			if (t instanceof Comparable) {
				t1 = (Comparable) t;
			} else {
				t1 = String.valueOf(t);
			}

			if (o instanceof Comparable) {
				o1 = (Comparable) o;
			} else {
				o1 = String.valueOf(o);
			}
			int result = t1.compareTo(o1);
			if (result != 0)
				return result;
		}
		return 0;
	}

	int length() {
		return data.length;
	}

	Object get(int i) {
		Object t = data[i];
		return t;
	}

	void set(int i, Object d) {
		data[i] = d;
	}

	int getLength(int j) {
		Object t = get(j);
		if (t instanceof String) {
			return ((String) t).length();
		}
		return String.valueOf(t).length();
	}

	Object[] get() {
		return data;
	}

}
