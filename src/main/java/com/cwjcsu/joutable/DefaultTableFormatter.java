package com.cwjcsu.joutable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.cwjcsu.joutable.Utils.*;

/**
 * 默认的表格格式化类
 * 
 * @author atlas
 * @date 2012-12-5
 */
public class DefaultTableFormatter implements TableFormatter {
	private static Logger log = Logger.getLogger(DefaultTableFormatter.class.getName());
	private List<CellFormatter> formatters = new ArrayList<CellFormatter>(0);

	private static final CellFormatter DEFAULT_CELLFORMATTER = new CellFormatter() {
		public String format(Object cell) {
			return cell == null ? "" : cell.toString();
		}

		public boolean accepts(Object cell) {
			return true;
		}
	};
	private final int overallWidth;
	private final int columnSeparatorWidth;

	private char headerSplitChar = '-';
	private char titlePadLeftChar = '=';
	private char titlePadRightChar = '=';
	/**
	 * 整个表格的缩进
	 */
	private String indent = "";
	/**
	 * 排序规则：按第一列从小到大排，相同时按第二列从小到达排...
	 */
	private boolean sort = false;

	private LengthCaculator lengthCaculator = new DefaultLengthCaculator();

	public DefaultTableFormatter() {
		this(120, 2);
	}

	public DefaultTableFormatter(int overallWidth, int columnSeparatorWidth) {
		this.overallWidth = overallWidth;
		this.columnSeparatorWidth = columnSeparatorWidth;
	}

	public void addCellFormatter(CellFormatter... formatters) {
		this.formatters.addAll(Arrays.asList(formatters));
	}

	private CellFormatter getCellFormatter(Object data) {
		for (CellFormatter cf : formatters) {
			if (cf.accepts(data)) {
				return cf;
			}
		}
		return DEFAULT_CELLFORMATTER;
	}

	public String format(Table table) {
		StringBuffer buffer = new StringBuffer();
		try {
			format(table, buffer);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return buffer.toString();
	}

	private void convert(List<Row> rows) {
		for (Row row : rows) {
			for (int i = 0; i < row.get().length; i++) {
				Object cell = row.get(i);
				cell = getCellFormatter(cell).format(cell);
				row.set(i, cell);
			}
		}
	}

	protected void sort(List<Row> rows) {
		Collections.sort(rows);
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public void setHeaderSplitChar(char headerSplitChar) {
		this.headerSplitChar = headerSplitChar;
	}

	public void setSupportChinese(boolean supportChinese) {
		this.lengthCaculator = supportChinese ? new DefaultLengthCaculator() : new AscciiLengthCaculator();
	}

	public void setTitleFillChar(char titleFillChar) {
		this.setTitlePadLeftChar(titleFillChar);
		this.setTitlePadRightChar(titleFillChar);
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public void setTitlePadLeftChar(char titlePadLeftChar) {
		this.titlePadLeftChar = titlePadLeftChar;
	}

	public void setTitlePadRightChar(char titlePadRightChar) {
		this.titlePadRightChar = titlePadRightChar;
	}

	public void setLengthCaculator(LengthCaculator lengthCaculator) {
		if (lengthCaculator == null) {
			throw new NullPointerException();
		}
		this.lengthCaculator = lengthCaculator;
	}

	protected void preProcess(List<Row> rows) {
		if (sort) {
			sort(rows);
		}
		convert(rows);
	}

	private int L(String string) {
		return lengthCaculator.length(string);
	}

	private int getLength(Row row, int index) {
		Object t = row.get(index);
		if (t instanceof String) {
			return L(((String) t));
		}
		return L(String.valueOf(t));
	}

	public void format(Table table, Appendable buffer) throws IOException {
		List<Row> rows = new ArrayList<Row>(table.getRows());
		preProcess(rows);

		String title = table.getTitle();
		// 列数
		int cols = table.getHeaders().length;
		// longestOfColumns[i]表示第i列的内容的最大长度
		int[] longestOfColumns = new int[cols];

		int[] headersWidth = new int[cols];
		for (int i = 0; i < table.getHeaders().length; i++) {
			String headeri = table.getHeaders()[i];
			if (isEmpty(headeri)) {
				throw new IllegalArgumentException("Empty header " + i);
			}
			headersWidth[i] = L(headeri);
		}
		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			for (int j = 0; j < row.length(); j++) {
				if (j >= longestOfColumns.length) {
					throw new IllegalArgumentException("column is " + longestOfColumns.length + ",but row " + i
							+ " has " + row.length() + " columns");
				}
				int len = getLength(row, j);
				len = Math.max(len, headersWidth[j]);
				longestOfColumns[j] = Math.max(longestOfColumns[j], len);
			}
		}
		int allHeaderWidth = sum(headersWidth);
		// 内容总宽度
		int totalContentWidth = sum(longestOfColumns);
		// 分割符总宽度
		int totalSeparatorWidth = (cols - 1) * columnSeparatorWidth;
		// 内容不换行表格的总宽度
		int totalCellWidth = totalContentWidth + totalSeparatorWidth;

		// 保证标题（加前后一个空格）只有一行
		int overallWidth = Math.max(this.overallWidth, L(title) + 2);
		// 保证标题只有一行
		overallWidth = Math.max(overallWidth, totalSeparatorWidth + allHeaderWidth);

		// 不换行是否足以容纳所有列
		boolean enough = totalCellWidth <= overallWidth;
		// 每个列内容的平均长度
		int cellLen = (overallWidth - totalSeparatorWidth) / cols;

		// 最终计算出来的每一列内容的最大宽度
		int[] widthOfColumns = new int[cols];
		// 最终计算出来的表格的总宽度
		int totalLength = totalSeparatorWidth;
		for (int i = 0; i < widthOfColumns.length; i++) {
			// if enough,那么不用换行，每列该多宽就多宽，否则取与平均值相比最少的一个值
			widthOfColumns[i] = enough ? longestOfColumns[i] : Math.min(cellLen, longestOfColumns[i]);
			// 保证标题只有一行
			widthOfColumns[i] = Math.max(widthOfColumns[i], headersWidth[i]);
			totalLength += widthOfColumns[i];
		}
		// 组装
		// 组装标题
		if (!isEmpty(title)) {
			int padHeader = (totalLength - L(title) + 1) / 2 - 2;
			buffer.append(indent);
			buffer.append(repeat(titlePadLeftChar, padHeader));
			buffer.append(' ');
			buffer.append(title);
			buffer.append(' ');
			buffer.append(repeat(titlePadRightChar, padHeader));
			buffer.append(LINE_SEPARATOR);
		}
		// 组装表头
		addRow(buffer, table.getHeaders(), widthOfColumns);
		// 组装表头与表体的分割线
		buffer.append(indent);
		for (int i = 0; i < cols; i++) {
			buffer.append(repeat(headerSplitChar, widthOfColumns[i]));
			buffer.append(repeat(' ', this.columnSeparatorWidth));
		}
		buffer.append(LINE_SEPARATOR);

		// 组装表体
		for (Row row : rows) {
			addRow(buffer, row.get(), widthOfColumns);
		}
	}

	public void addRow(Appendable buffer, Object[] row, int[] wideOfColumns) throws IOException {
		String[] nextRow;
		boolean repeart = true;
		while (repeart) {
			buffer.append(indent);
			repeart = false;
			nextRow = new String[row.length];
			for (int i = 0; i < row.length; i++) {
				String td = row[i] != null ? row[i].toString() : "";
				int w = wideOfColumns[i];
				int pad = w - L(td);
				if (td == null) {
					buffer.append(repeat(' ', pad));
				} else {
					if (pad >= 0) {
						buffer.append(td);
						buffer.append(repeat(' ', pad));
					} else if (pad < 0) {
						repeart = true;
						buffer.append(lengthCaculator.substring(td, 0, w));
						nextRow[i] = lengthCaculator.substring(td, w);
					}
				}
				if (i < row.length - 1)
					buffer.append(repeat(' ', this.columnSeparatorWidth));
			}
			buffer.append(LINE_SEPARATOR);
			if (repeart) {
				row = nextRow;
			}
		}
	}
	// ---------- utilities

}
