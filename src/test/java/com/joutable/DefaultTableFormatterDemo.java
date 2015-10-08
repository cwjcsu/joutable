package com.joutable;

import java.util.Random;

import org.junit.Test;

import com.joutable.DefaultTable;
import com.joutable.DefaultTableFormatter;

/**
 * 
 * @author atlas
 * @date 2012-12-5
 */
public class DefaultTableFormatterDemo {
	@Test
	public void testBasic() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("Resource  Status");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRows(1));
		dt.addRow(getRows(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testLongCell() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("Resource  Status");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRowsRandom(1));
		dt.addRow(getRowsRandom(2));
		dt.addRow(getRowsRandom(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testLongTitl() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("Longongongongongongongongongongongongongongongongongongongongongongongongongongongongongongong title");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRows(1));
		dt.addRow(getRows(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testLongHeaders() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A title");
		dt.setHeaders(new String[] { "LLLLLLLLLLLLLLLLLLLLLLLLLLLlongId",
				"Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRows(1));
		dt.addRow(getRows(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testShortWholeWidth() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("Resource  Status");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		// dt.addRow(getRows(0));
		// dt.addRow(getRowsRandom(1));
		// dt.addRow(getRowsRandom(3));
		// dt.addRow(getRowsRandom(4));
		DefaultTableFormatter dtf = new DefaultTableFormatter(50, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testSort1() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted title");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node1" });
		dt.addRow(new Object[] { 1, "Node1" });
		dt.addRow(new Object[] { 4, "Node1" });
		dt.addRow(new Object[] { 2, "Node1" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testSort2() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted title");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node4" });
		dt.addRow(new Object[] { 1, "Node3" });
		dt.addRow(new Object[] { 3, "Node2" });
		dt.addRow(new Object[] { 1, "Node1" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testSort3() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted title");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node1" });
		dt.addRow(new Object[] { 1, "Node1" });
		dt.addRow(new Object[] { 4, "Node1" });
		dt.addRow(new Object[] { 2, "Node1" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testIndent() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted title");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node1" });
		dt.addRow(new Object[] { 1, "Node1" });
		dt.addRow(new Object[] { 4, "Node1" });
		dt.addRow(new Object[] { 2, "Node1" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setIndent("  *  ");
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testArrowTitlePad() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("arrow padded title");
		dt.setHeaders(new String[] { "Id", "Name", "State" });
		dt.addRow(new Object[] { 1, "Node1", "Running" });
		dt.addRow(new Object[] { 2, "Node2", "Running" });
		dt.addRow(new Object[] { 3, "Node3", "Running" });
		dt.addRow(new Object[] { 4, "Node4", "Running" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(240, 2);
		dtf.setTitlePadLeftChar('>');
		dtf.setTitlePadRightChar('>');
		System.out.println(dtf.format(dt));
	}

	public static String[] getRows(int row) {
		return new String[] { "id" + row, "Node" + row, row + "sss", "Stopped" };
	}

	static Random r = new Random();

	public static String[] getRowsRandom(int row) {
		return new String[] { "id" + row, randomStr(10 * row) + row,
				row + "sss" + r.nextLong(), "Stopped" };
	}

	/**
	 * 生成指定长度由小写字母和数字组成的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randomStr(int length) {
		char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		String result = "";
		for (int i = 0; i < length; i++) {
			// 生成下标的随机数
			Random random = new Random();
			int index = random.nextInt(chars.length);
			char tempChar = chars[index];
			result += tempChar;
		}
		return result;
	}
}