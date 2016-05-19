package com.cwjcsu.joutable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.cwjcsu.joutable.CellFormatter;
import com.cwjcsu.joutable.DefaultTable;
import com.cwjcsu.joutable.DefaultTableFormatter;

/**
 * 
 * @author atlas
 * @date 2012-12-5
 */
public class DefaultTableFormatterDemo {
	@Test
	public void testBasic0() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("Aacademic Record Table");
		dt.setHeaders(new String[] { "Id", "Name", "Grade", "Remark" });
		dt.addRow(new Object[] { 1001, "Jack Bower", "A", "Good at fighting" });
		dt.addRow(new Object[] { 1002, "Vincent Willem  Van Gogh", "A+","Good at painting" });
		dt.addRow(new Object[] { 1003, "Jone Doe", "B", "Noop" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testBasic() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A common table");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRows(1));
		dt.addRow(getRows(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testLongTitle() {
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
		dt.setHeaders(new String[] { "LLLLLLLLLLLLLLLLLLLLLLLLLLLlongHeader",
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
		dt.setTitle("Short wrapped column");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRowsRandom(1));
		dt.addRow(getRowsRandom(3));
		dt.addRow(getRowsRandom(4));
		DefaultTableFormatter dtf = new DefaultTableFormatter(50, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testSort() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted table");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node3" });
		dt.addRow(new Object[] { 1, "Node1" });
		dt.addRow(new Object[] { 4, "Node4" });
		dt.addRow(new Object[] { 2, "Node2Y" });
		dt.addRow(new Object[] { 2, "Node2X" });
		dt.addRow(new Object[] { 2, "Node2" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setSort(true);
		
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testSort2() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A sorted table2");
		dt.setHeaders(new String[] { "Id", "Name" });
		dt.addRow(new Object[] { 3, "Node4" });
		dt.addRow(new Object[] { 1, "Node3" });
		dt.addRow(new Object[] { 3, "Node2" });
		dt.addRow(new Object[] { 1, "Node1" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setSort(true);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testIndent() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A intent table");
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
		dt.addRow(new Object[] { 1, "LOOOOOOOOOOOOOOOOOOOOOOOOng",
				"LOOOOOOOOOOOOOOOOOOOOOOOOng column" });
		dt.addRow(new Object[] { 2, "Node2", "Running" });
		dt.addRow(new Object[] { 3, "Node3", "Running" });
		dt.addRow(new Object[] { 4, "Node4", "Running" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(240, 2);
		dtf.setTitlePadLeftChar('<');
		dtf.setTitlePadRightChar('>');
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testLongCellWrapped() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("wrapped cell title");
		dt.setHeaders(new String[] { "Id", "Name", "State" });
		dt.addRow(new Object[] {
				1,
				"NOOOOOOOOOOOOOOOOOOOOOOoooooooooooooooooooooooooooooooooooooooooooooOOde1",
				"Running" });
		dt.addRow(new Object[] { 2, "Node2", "Running" });
		dt.addRow(new Object[] { 3, "Node3", "Running" });
		dt.addRow(new Object[] { 4, "Node4", "Running" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(80, 2);
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testDateAsCell() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A date cell table");
		dt.setHeaders(new String[] { "Id", "Name", "Date" });
		dt.addRow(new Object[] { 1, "Node1", randomDate() });
		dt.addRow(new Object[] { 2, "Node2", randomDate() });
		dt.addRow(new Object[] { 3, "Node3", randomDate() });
		dt.addRow(new Object[] { 4, "Node4", "Today" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(80, 2);
		dtf.addCellFormatter(new CellFormatter() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			public String format(Object cell) {
				return sdf.format((Date) cell);
			}

			public boolean accepts(Object cell) {
				return cell instanceof Date;
			}
		});
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testStarSeperatedHeader() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("A custom header seperator table");
		dt.setHeaders(new String[] { "Id", "Name", "Node", "Status" });
		dt.addRow(getRows(0));
		dt.addRow(getRows(1));
		dt.addRow(getRows(2));
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setHeaderSplitChar('*');
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testChinese0() {
		// TODO 添加中文支持
		DefaultTable dt = new DefaultTable();
		dt.setTitle("学生成绩表");
		dt.setHeaders(new String[] { "学号", "姓名", "成绩", "备注" });
		dt.addRow(new Object[] { 1001, "秦始皇", 80 });
		dt.addRow(new Object[] { 1002, "秦始皇", 80 });
		dt.addRow(new Object[] { 1003, "Jack Bouwer", 90, "" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setHeaderSplitChar('*');
		System.out.println(dtf.format(dt));
	}

	

	@Test
	public void testChinese1() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("一个中文表格");
		dt.setHeaders(new String[] { "编号", "名称" });
		dt.addRow(new Object[] { 3, "秦始皇" });
		dt.addRow(new Object[] { 1, "刘邦" });
		dt.addRow(new Object[] { 4, "曹操" });
		dt.addRow(new Object[] { 2, "刘彻" });
		dt.addRow(new Object[] { 5, "John Doe" });
		DefaultTableFormatter dtf = new DefaultTableFormatter(100, 2);
		dtf.setIndent("");
		System.out.println(dtf.format(dt));
	}

	@Test
	public void testChinese2() {
		DefaultTable dt = new DefaultTable();
		dt.setTitle("一个中文表格");
		dt.setHeaders(new String[] { "编号", "内容","备注" });
		dt.addRow(new Object[] { 3, "秦始皇" ,""});
		dt.addRow(new Object[] { 1, "刘邦" ,""});
		dt.addRow(new Object[] { 4, "曹操" });
		dt.addRow(new Object[] { 2, "刘彻金屋藏娇-典故正史无载，来源于志怪小说《汉武故事》其核心人物有两个。。。“娇”就是指陈氏，汉武帝刘彻的第一任皇后，后因骄横、无子与巫蛊被废黜" ,"汉武帝"});
		dt.addRow(new Object[] { 5, "John Doe" ,"Who knows"});
		DefaultTableFormatter dtf = new DefaultTableFormatter(130, 3);
		System.out.println(dtf.format(dt));
	}
	
	private static Random r = new Random();

	public static Date randomDate() {
		int n = 1000 * 60 * 60 * 24;// milliseconds in a day
		return new Date(System.currentTimeMillis() + r.nextInt(n) - n / 2);
	}

	public static String[] getRowsRandom(int row) {
		return new String[] { "id" + row, randomStr(10 * row) + row,
				row + "sss" + r.nextLong(), "Stopped" };
	}

	public static String[] getRows(int row) {
		return new String[] { "id" + row, "Node" + row, row + "sss", "Stopped" };
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
			int index = r.nextInt(chars.length);
			char tempChar = chars[index];
			result += tempChar;
		}
		return result;
	}
}