package com.joutable;

import static java.lang.System.getProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 默认的格式化工具
 * @author atlas
 * @date 2012-12-5
 */
/**
 * @author atlas
 * @date 2015年10月8日
 */
public class DefaultTableFormatter implements TableFormatter {
	private static Logger log = Logger.getLogger(DefaultTableFormatter.class
			.getName());

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
	private String lineSeparator = getProperty("line.separator");
	private char titlePadLeftChar = '=';
	private char titlePadRightChar = '=';
	private char headerSplitChar = '-';
	/**
	 * 整个表格的缩进
	 */
	private String indent = "";
	/**
	 * 排序规则：按第一列从小到大排，相同时按第二列从小到达排...
	 */
	private boolean sort = false;

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

	@Override
	public String format(Table table) {
		StringBuilder buffer = new StringBuilder();
		try {
			format(table, buffer);
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage(), e);
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

	/**
	 * 设置整个表格的缩进
	 * 
	 * @param indent
	 */
	public void setIndent(String indent) {
		this.indent = indent;
	}

	/**
	 * 设置表头和表体直接的分隔符，默认是'-'
	 * 
	 * @param headerSplitChar
	 */
	public void setHeaderSplitChar(char headerSplitChar) {
		this.headerSplitChar = headerSplitChar;
	}

	/**
	 * 设置填充标题左边剩余空间使用的字符，默认是'='
	 * 
	 * @param titleFillChar
	 */

	public void setTitlePadLeftChar(char titlePadLeftChar) {
		this.titlePadLeftChar = titlePadLeftChar;
	}

	/**
	 * 设置填充标题右边剩余空间使用的字符，默认是'='
	 * 
	 * @param titlePadRightChar
	 */
	public void setTitlePadRightChar(char titlePadRightChar) {
		this.titlePadRightChar = titlePadRightChar;
	}

	/**
	 * 
	 * @param lineSeparator
	 */
	public void setLineSeparator(String lineSeparator) {
		if (!isEmpty(lineSeparator)) {
			this.lineSeparator = lineSeparator;
		}
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	protected void preProcess(List<Row> rows) {
		if (sort) {
			sort(rows);
		}
		convert(rows);
	}

	@Override
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
			headersWidth[i] = table.getHeaders()[i].length();
		}
		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			for (int j = 0; j < row.length(); j++) {
				if (j >= longestOfColumns.length) {
					throw new IllegalArgumentException("column is "
							+ longestOfColumns.length + ",but row " + i
							+ " has " + row.length() + " columns");
				}
				int len = row.getLength(j);
				headersWidth[j] = table.getHeaders()[j] != null ? table
						.getHeaders()[j].length() : 0;
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
		int overallWidth = Math.max(this.overallWidth, title.length() + 2);
		// 保证标题只有一行
		overallWidth = Math.max(overallWidth, totalSeparatorWidth
				+ allHeaderWidth);

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
			widthOfColumns[i] = enough ? longestOfColumns[i] : Math.min(
					cellLen, longestOfColumns[i]);
			// 保证标题只有一行
			widthOfColumns[i] = Math.max(widthOfColumns[i], headersWidth[i]);
			totalLength += widthOfColumns[i];
		}
		// 组装
		// 组装标题
		int padHeader = (totalLength - title.length() + 1) / 2 - 2;// 不让标题与pad字符连接在一起，左右各空一格
		buffer.append(indent);
		buffer.append(repeat(titlePadLeftChar, padHeader));
		buffer.append(' ');
		buffer.append(title);
		buffer.append(' ');
		buffer.append(repeat(titlePadRightChar, padHeader));
		buffer.append(lineSeparator);
		// 组装表头
		addRow(buffer, table.getHeaders(), widthOfColumns);
		// 组装表头与表体的分割线
		buffer.append(indent);
		for (int i = 0; i < cols; i++) {
			buffer.append(repeat(headerSplitChar, widthOfColumns[i]));
			buffer.append(repeat(' ', this.columnSeparatorWidth));
		}
		buffer.append(lineSeparator);

		// 组装表体
		for (Row row : rows) {
			addRow(buffer, row.get(), widthOfColumns);
		}
	}

	private void addRow(Appendable buffer, Object[] row, int[] wideOfColumns)
			throws IOException {
		buffer.append(indent);
		String[] nextRow = new String[row.length];
		boolean repeart = true;
		while (repeart) {
			repeart = false;
			nextRow = new String[row.length];
			for (int i = 0; i < row.length; i++) {
				String td = row[i] != null ? row[i].toString() : "";
				int w = wideOfColumns[i];
				int pad = w - (td != null ? td.length() : 0);
				if (td == null) {
					buffer.append(repeat(' ', pad));
				} else {
					if (pad >= 0) {
						buffer.append(td);
						buffer.append(repeat(' ', pad));
					} else if (pad < 0) {//当前列需要换行
						repeart = true;
						buffer.append(td.subSequence(0, w));
						nextRow[i] = td.substring(w);
					}
				}
				if (i < row.length - 1)
					buffer.append(repeat(' ', this.columnSeparatorWidth));
			}
			buffer.append(lineSeparator);
			if (repeart) {
				row = nextRow;
			}
		}
	}

	// ---------- utilities

	public static String repeat(char ch, int count) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < count; ++i)
			buffer.append(ch);
		return buffer.toString();
	}

	public static boolean isEmpty(Object str) {
		if (str == null || "".equals(str.toString().trim()))
			return true;
		return false;
	}

	public static int sum(int[] d) {
		int sum = 0;
		for (int d0 : d) {
			sum += d0;
		}
		return sum;
	}
}
