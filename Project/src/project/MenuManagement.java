package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
import java.sql.*;

public class MenuManagement extends JFrame implements ActionListener, MouseListener {
	Vector<String> columnName;
	Vector<Vector<String>> rowData;
	JTable table = null;
	DefaultTableModel model = null;
	JScrollPane tableSP;
	int row = -1;

	JLabel cg;
	JComboBox<String> category;
	JButton cg_m, insertB, updateB, deleteB;
	String selectedMenuCode, selectedCategory, selectedMenuName, selectedMenuPrice, selectedMenuStock;

	MenuManagement() {
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel center = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.NORTH);
		ct.add(center, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout());
		cg = new JLabel("카테고리  :  ");
		combobox();
		category.addActionListener(this);
		cg_m = new JButton("카테고리 관리");
		cg_m.addActionListener(this);
		top.add(cg);
		top.add(category);
		top.add(cg_m);

		columnName = new Vector<String>();
		columnName.add("메뉴 코드");
		columnName.add("카테고리");
		columnName.add("메뉴");
		columnName.add("가격");
		columnName.add("재고");

		rowData = new Vector<Vector<String>>();
		model = new DefaultTableModel(rowData, columnName);
		table = new JTable(model);
		tableSP = new JScrollPane(table);
		table.addMouseListener(this);
		showmenu();

		center.setLayout(new FlowLayout());
		center.add(tableSP);

		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
		insertB = new JButton("추가");
		updateB = new JButton("수정");
		deleteB = new JButton("삭제");
		insertB.addActionListener(this);
		updateB.addActionListener(this);
		deleteB.addActionListener(this);
		bottom.add(insertB);
		bottom.add(updateB);
		bottom.add(deleteB);
	}

	public void combobox() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 로드되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			category = new JComboBox<String>();
			category.removeAllItems();

			Statement dbSt = con.createStatement();
			String strSql = "SELECT CategoryName FROM category";
			ResultSet result = dbSt.executeQuery(strSql);

			category.addItem("전체");

			while (result.next()) {
				category.addItem(result.getString("CategoryName"));
			}

			dbSt.close();
			con.close();

		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public Vector<String> getCategoryItems() {
		Vector<String> categoryItems = new Vector<>();
		for (int i = 0; i < category.getItemCount(); i++) {
			String item = category.getItemAt(i);
			if (!item.equals("전체")) {
				categoryItems.add(item);
			}
		}
		return categoryItems;
	}

	public void showmenu() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 로드되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			rowData.clear();

			Statement dbSt = con.createStatement();
			String selectedCategory = category.getSelectedItem().toString();
			String strSql;

			if (selectedCategory.equals("전체"))
				strSql = "SELECT MenuCode, CategoryName, MenuName, MenuPrice, MenuStock FROM menu";
			else
				strSql = "SELECT MenuCode, CategoryName, MenuName, MenuPrice, MenuStock FROM menu WHERE CategoryName = '"
						+ selectedCategory + "'";

			ResultSet result = dbSt.executeQuery(strSql);

			while (result.next()) {
				Vector<String> rs = new Vector<String>();
				rs.add(result.getString("MenuCode"));
				rs.add(result.getString("CategoryName"));
				rs.add(result.getString("MenuName"));
				rs.add(result.getString("MenuPrice"));
				rs.add(result.getString("MenuStock"));
				rowData.add(rs);
			}

			model.setDataVector(rowData, columnName);
			model.fireTableDataChanged();

			dbSt.close();
			con.close();

		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getActionCommand().equals("추가")) {
			MenuInsert mi = new MenuInsert(this);
			mi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mi.setSize(300, 300);
			mi.setLocation(900, 400);
			mi.setVisible(true);
		}

		else if (ae.getActionCommand().equals("수정")) {
			if (row != -1) {
				MenuUpdate mu = new MenuUpdate(this);
				mu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				mu.setSize(300, 300);
				mu.setLocation(900, 400);
				mu.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "메뉴를 선택해주세요.", "메뉴 선택 실패", JOptionPane.ERROR_MESSAGE);
			}
		}

		else if (ae.getActionCommand().equals("삭제")) {
			if (row != -1) {
				MenuDelete md = new MenuDelete(this);
				md.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				md.setSize(300, 100);
				md.setLocation(900, 400);
				md.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "메뉴를 선택해주세요.", "메뉴 선택 실패", JOptionPane.ERROR_MESSAGE);
			}
		}

		else if (ae.getActionCommand().equals("카테고리 관리")) {
			CategoryManagement cm = new CategoryManagement(this);
			cm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			cm.setSize(300, 100);
			cm.setLocation(900, 400);
			cm.setVisible(true);
		} else
			showmenu();
	}

	public void mouseClicked(MouseEvent ae) {
		row = table.getSelectedRow();
		if (row != -1) {
			selectedMenuCode = (String) model.getValueAt(row, 0);
			selectedCategory = (String) model.getValueAt(row, 1);
			selectedMenuName = (String) model.getValueAt(row, 2);
			selectedMenuPrice = (String) model.getValueAt(row, 3);
			selectedMenuStock = (String) model.getValueAt(row, 4);
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
