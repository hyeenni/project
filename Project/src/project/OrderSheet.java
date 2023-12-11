package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
import java.sql.*;

public class OrderSheet extends JFrame implements ActionListener, MouseListener {
	Vector<String> columnName, details;
	Vector<Vector<String>> rowData, detailsData;
	JTable table = null, detailstable = null;
	DefaultTableModel model = null, detailsmodel = null;
	JScrollPane tableSP, detailstableSP;
	int row = -1, detailsrow;
	String table_n, order_t, total_a, order_d, selectedYear, selectedMonth, selectedDay;

	JLabel odate, tn, odivision, opayment, odetails;
	JComboBox<String> order_year, order_month, order_day, table_name, order_division;
	JButton checkB, cancelB, completeB;
	Calendar calendar = Calendar.getInstance();
	int year, month, day;

	OrderSheet() {
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout(0, 20));
		JPanel top = new JPanel();
		JPanel center = new JPanel();
		ct.add(top, BorderLayout.NORTH);
		ct.add(center, BorderLayout.CENTER);

		top.setLayout(new BorderLayout());

		JPanel topleft = new JPanel();
		topleft.setLayout(new FlowLayout());
		odate = new JLabel("영업일자 ");
		order_year = new JComboBox<>();
		order_month = new JComboBox<>();
		order_day = new JComboBox<>();
		setYear();
		setMonth();
		setDay();
		tn = new JLabel("테이블명 ");
		table_name = new JComboBox<>();
		settable_name();
		odivision = new JLabel("구분 ");
		order_division = new JComboBox<>();
		setorder_division();
		topleft.add(odate);
		topleft.add(order_year);
		topleft.add(order_month);
		topleft.add(order_day);
		topleft.add(tn);
		topleft.add(table_name);
		topleft.add(odivision);
		topleft.add(order_division);

		JPanel topright = new JPanel();
		topright.setLayout(new FlowLayout());
		checkB = new JButton("조   회");
		checkB.addActionListener(this);
		topright.add(checkB);

		top.add(topleft, BorderLayout.WEST);
		top.add(topright, BorderLayout.EAST);

		center.setLayout(new BorderLayout());

		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());

		opayment = new JLabel("주문 및 결제내역");

		columnName = new Vector<String>();
		columnName.add("*");
		columnName.add("테이블명");
		columnName.add("주문시간");
		columnName.add("총 결제금액");
		columnName.add("구분");

		rowData = new Vector<Vector<String>>();
		model = new DefaultTableModel(rowData, columnName);
		table = new JTable(model);
		tableSP = new JScrollPane(table);
		table.addMouseListener(this);

		JPanel leftbottom = new JPanel();
		leftbottom.setLayout(new FlowLayout());

		completeB = new JButton("결 제 완 료");
		completeB.addActionListener(this);

		cancelB = new JButton("결 제 취 소");
		cancelB.addActionListener(this);

		leftbottom.add(completeB);
		leftbottom.add(cancelB);

		left.add(opayment, BorderLayout.NORTH);
		left.add(tableSP, BorderLayout.CENTER);
		left.add(leftbottom, BorderLayout.SOUTH);

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());

		odetails = new JLabel("상세 주문 내역");

		details = new Vector<String>();
		details.add("*");
		details.add("상품명");
		details.add("수량");
		details.add("금액");
		details.add("주문 시간");

		detailsData = new Vector<Vector<String>>();
		detailsmodel = new DefaultTableModel(detailsData, details);
		detailstable = new JTable(detailsmodel);
		detailstableSP = new JScrollPane(detailstable);
		detailstable.addMouseListener(this);

		right.add(odetails, BorderLayout.NORTH);
		right.add(detailstableSP, BorderLayout.CENTER);

		center.add(left, BorderLayout.WEST);
		center.add(right, BorderLayout.EAST);

	}

	void setYear() {
		year = calendar.get(Calendar.YEAR);
		order_year.removeAllItems();
		for (int i = year + 10; i >= year - 10; i--)
			order_year.addItem(String.valueOf(i));
		order_year.setSelectedItem(String.valueOf(year));
	}

	void setMonth() {
		month = calendar.get(Calendar.MONTH) + 1;
		order_month.removeAllItems();
		for (int i = 1; i <= 12; i++)
			order_month.addItem(addZeroString(i));
		order_month.setSelectedItem(addZeroString(month));
	}

	void setDay() {
		day = calendar.get(Calendar.DAY_OF_MONTH);
		order_day.removeAllItems();
		for (int i = 1; i <= 31; i++)
			order_day.addItem(addZeroString(i));
		order_day.setSelectedItem(addZeroString(day));
	}

	private String addZeroString(int i) {
		String value = Integer.toString(i);
		if (value.length() == 1)
			value = "0" + value;
		return value;
	}

	void settable_name() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 로드되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			Statement dbSt = con.createStatement();
			String strSql = "SELECT DISTINCT TableName FROM `order` ORDER BY TableName ASC";
			ResultSet result = dbSt.executeQuery(strSql);

			table_name.addItem("전체");

			while (result.next()) {
				table_name.addItem(result.getString("TableName"));
			}

			dbSt.close();
			con.close();

		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	void setorder_division() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 로드되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			Statement dbSt = con.createStatement();
			String strSql = "SELECT DISTINCT OrderDivision FROM `order`";
			ResultSet result = dbSt.executeQuery(strSql);

			order_division.addItem("전체");

			while (result.next()) {
				order_division.addItem(result.getString("OrderDivision"));
			}

			dbSt.close();
			con.close();

		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("조   회")) {
			checkOrderData();
		} else if (ae.getActionCommand().equals("결 제 취 소")) {
			updateOrderDivision("결제취소", "결제 취소", "이미 결제 취소된 주문입니다.", "결제 취소 실패");
		} else {
			updateOrderDivision("결제완료", "결제 완료", "이미 결제 완료된 주문입니다.", "결제 완료 실패");
		}
	}

	private void checkOrderData() {
		selectedYear = (String) order_year.getSelectedItem();
		selectedMonth = (String) order_month.getSelectedItem();
		selectedDay = (String) order_day.getSelectedItem();
		String selectedDate = selectedYear + "-" + selectedMonth + "-" + selectedDay;
		String selectedTableName = (String) table_name.getSelectedItem();
		String selectedOrderDivision = (String) order_division.getSelectedItem();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			String strSql;
			PreparedStatement ppSt;

			if (selectedTableName.equals("전체") && selectedOrderDivision.equals("전체")) {
				strSql = "SELECT * FROM `order` WHERE DATE(OrderDate) = ?";
				ppSt = con.prepareStatement(strSql);
				ppSt.setString(1, selectedDate);
			} else if (!selectedTableName.equals("전체") && selectedOrderDivision.equals("전체")) {
				strSql = "SELECT * FROM `order` WHERE DATE(OrderDate) = ? AND TableName = ?";
				ppSt = con.prepareStatement(strSql);
				ppSt.setString(1, selectedDate);
				ppSt.setString(2, selectedTableName);
			} else if (selectedTableName.equals("전체") && !selectedOrderDivision.equals("전체")) {
				strSql = "SELECT * FROM `order` WHERE DATE(OrderDate) = ? AND OrderDivision = ?";
				ppSt = con.prepareStatement(strSql);
				ppSt.setString(1, selectedDate);
				ppSt.setString(2, selectedOrderDivision);
			} else {
				strSql = "SELECT * FROM `order` WHERE DATE(OrderDate) = ? AND TableName = ? AND OrderDivision = ?";
				ppSt = con.prepareStatement(strSql);
				ppSt.setString(1, selectedDate);
				ppSt.setString(2, selectedTableName);
				ppSt.setString(3, selectedOrderDivision);
			}

			ResultSet result = ppSt.executeQuery();

			rowData.clear();
			detailsData.clear();

			while (result.next()) {
				Vector<String> rs = new Vector<String>();
				rs.add(result.getString("OrderCode"));
				rs.add(result.getString("TableName"));
				rs.add(result.getString("OrderDate"));
				rs.add(result.getString("OrderTotal"));
				rs.add(result.getString("OrderDivision"));
				rowData.add(rs);
			}

			if (!rowData.isEmpty()) {
				model.setDataVector(rowData, columnName);
				table.setModel(model);
			} else {
				model.setDataVector(new Vector<>(), columnName);
				table.setModel(model);
			}

			detailsmodel.setDataVector(detailsData, details);
			detailstable.setModel(detailsmodel);

			ppSt.close();
			con.close();

		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}

	private void updateOrderDivision(String newOrderDivision, String successMessage, String alreadyDoneMessage,
			String failureMessage) {
		if (row != -1) {
			String selectedOrderDivision = (String) table.getValueAt(row, 4);
			String selectedOrderCode = (String) table.getValueAt(row, 0);

			if (!selectedOrderDivision.equals(newOrderDivision)) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
							"root", "0000");
					System.out.println("DB 연결 완료.");

					String strSql = "UPDATE `order` SET OrderDivision = ? WHERE OrderCode = ?";
					PreparedStatement ppSt = con.prepareStatement(strSql);

					ppSt.setString(1, newOrderDivision);
					ppSt.setString(2, selectedOrderCode);
					int result = ppSt.executeUpdate();

					if (result > 0)
						JOptionPane.showMessageDialog(this, successMessage, successMessage,
								JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(this, failureMessage, "에러", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e) {
					System.err.println("드라이버 로드에 실패했습니다.");
				} catch (SQLException e) {
					System.out.println("SQLException : " + e.getMessage());
				}
			} else
				JOptionPane.showMessageDialog(this, alreadyDoneMessage, failureMessage, JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(this, "주문을 선택해주세요.", "주문 선택 실패", JOptionPane.ERROR_MESSAGE);
		row = -1;
	}

	public void mouseClicked(MouseEvent ae) {
		if (ae.getSource() == table) {
			row = table.getSelectedRow();
			if (row != -1) {
				String selectedOrderCode = (String) table.getValueAt(row, 0);

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					System.err.println("JDBC 드라이버가 정상적으로 로드되었습니다.");

					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
							"root", "0000");
					System.out.println("DB 연결 완료.");

					Statement dbSt = con.createStatement();
					String strSql = "SELECT od.MenuCode, m.MenuName, od.Quantity, od.Amount, od.OrderCode, TIME(o.OrderDate) AS OrderTime "
							+ "FROM orderdetails od " + "LEFT JOIN menu m ON od.MenuCode = m.MenuCode "
							+ "LEFT JOIN `order` o ON od.OrderCode = o.OrderCode " + "WHERE od.OrderCode = "
							+ selectedOrderCode;

					ResultSet result = dbSt.executeQuery(strSql);

					detailsData.clear();

					while (result.next()) {
						Vector<String> rs = new Vector<String>();
						rs.add(result.getString("OrderCode"));
						rs.add(result.getString("MenuName"));
						rs.add(result.getString("Quantity"));
						rs.add(result.getString("Amount"));
						rs.add(result.getString("OrderTime"));
						detailsData.add(rs);
					}

					detailsmodel.setDataVector(detailsData, details);
					detailstable.setModel(detailsmodel);

					dbSt.close();
					con.close();

				} catch (ClassNotFoundException e) {
					System.err.println("드라이버 로드에 실패했습니다.");
				} catch (SQLException e) {
					System.out.println("SQLException : " + e.getMessage());
				}
			}
		}
	}

	public void mousePressed(MouseEvent ae) {
	}

	public void mouseReleased(MouseEvent ae) {
	}

	public void mouseEntered(MouseEvent ae) {
	}

	public void mouseExited(MouseEvent ae) {
	}
}
