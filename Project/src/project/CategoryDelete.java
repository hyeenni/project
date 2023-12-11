package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class CategoryDelete extends JFrame implements ActionListener {
	JLabel message;
	JButton deleteB, cancelB;
	MenuManagement menu;
	CategoryManagement categorymanagement;

	CategoryDelete(MenuManagement menu, CategoryManagement categorymanagement) {
		setTitle("카테고리 수정");
		this.menu = menu;
		this.categorymanagement = categorymanagement;
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout(FlowLayout.CENTER));
		message = new JLabel(setmassage());
		top.add(message);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		deleteB = new JButton("삭제");
		cancelB = new JButton("취소");
		deleteB.addActionListener(this);
		cancelB.addActionListener(this);
		bottom.add(deleteB);
		bottom.add(cancelB);
	}

	private String setmassage() {
		String categoryName = categorymanagement.selectedCategory;
		if (menuExistsInCategory(categoryName)) {
			return "카테고리에 메뉴가 남아있습니다.  카테고리와 메뉴를 모두 삭제하시겠습니까?";
		} else {
			return "정말 삭제하시겠습니까?";
		}
	}

	public boolean menuExistsInCategory(String categoryName) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");

			String strSql = "SELECT COUNT(*) FROM menu WHERE CategoryName = ?";
			try (PreparedStatement ppSt = con.prepareStatement(strSql)) {
				ppSt.setString(1, categoryName);
				ResultSet rs = ppSt.executeQuery();

				if (rs.next()) {
					int count = rs.getInt(1);
					return count > 0;
				}
			}

			con.close();
		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}

		return false;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("취소"))
			dispose();
		else {
			String categoryName = categorymanagement.selectedCategory;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
						"root", "0000");
				System.out.println("DB 연결 완료.");

				con.setAutoCommit(false);

				String mdSql = "DELETE FROM menu WHERE CategoryName = ?";
				try (PreparedStatement mdSt = con.prepareStatement(mdSql)) {
					mdSt.setString(1, categoryName);
					mdSt.executeUpdate();
				}

				String cdSql = "DELETE FROM category WHERE CategoryName = ?";
				try (PreparedStatement cdSt = con.prepareStatement(cdSql)) {
					cdSt.setString(1, categoryName);
					cdSt.executeUpdate();
				}

				con.commit();
				con.setAutoCommit(true);

				JOptionPane.showMessageDialog(this, "카테고리가 삭제되었습니다.", "카테고리 삭제 완료", JOptionPane.INFORMATION_MESSAGE);

				con.close();
				menu.combobox();
				dispose();

				menu.showmenu();

			} catch (ClassNotFoundException e) {
				System.err.println("드라이버 로드에 실패했습니다.");
			} catch (SQLException e) {
				System.out.println("SQLException : " + e.getMessage());
			}
		}
	}
}
