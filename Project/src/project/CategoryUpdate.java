package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class CategoryUpdate extends JFrame implements ActionListener {
	JLabel cg;
	JTextField category_name;
	JButton updateB, cancelB;
	MenuManagement menu;
	CategoryManagement categorymanagement;

	CategoryUpdate(MenuManagement menu, CategoryManagement categorymanagement) {
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
		cg = new JLabel("카테고리명 : ");
		category_name = new JTextField(10);
		category_name.setText(categorymanagement.selectedCategory);
		top.add(cg);
		top.add(category_name);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		updateB = new JButton("수정");
		cancelB = new JButton("취소");
		updateB.addActionListener(this);
		cancelB.addActionListener(this);
		bottom.add(updateB);
		bottom.add(cancelB);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("취소")) {
			category_name.setText(categorymanagement.selectedCategory);
		} else {
			String oldCategoryName = categorymanagement.selectedCategory;
			String newCategoryName = category_name.getText();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
						"root", "0000");
				System.out.println("DB 연결 완료.");

				if (newCategoryName.isEmpty()) {
					JOptionPane.showMessageDialog(this, "모든 항목을 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					con.setAutoCommit(false);

					String umSql = "UPDATE menu SET CategoryName = ? WHERE CategoryName = ?";
					try (PreparedStatement muSt = con.prepareStatement(umSql)) {
						muSt.setString(1, newCategoryName);
						muSt.setString(2, oldCategoryName);
						muSt.executeUpdate();
					}

					String ucSql = "UPDATE category SET CategoryName = ? WHERE CategoryName = ?";
					try (PreparedStatement cuSt = con.prepareStatement(ucSql)) {
						cuSt.setString(1, newCategoryName);
						cuSt.setString(2, oldCategoryName);
						cuSt.executeUpdate();
					}

					con.commit();
				} catch (SQLException e) {
					con.rollback();
				} finally {
					con.setAutoCommit(true);
				}

				JOptionPane.showMessageDialog(this, "카테고리가 수정되었습니다.", "카테고리 수정 완료", JOptionPane.INFORMATION_MESSAGE);

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
