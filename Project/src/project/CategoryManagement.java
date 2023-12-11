package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class CategoryManagement extends JFrame implements ActionListener {
	JButton insertB, updateB, deleteB;
	Vector<String> categoryItems;
	JComboBox<String> categoryCopy;
	String selectedCategory;
	MenuManagement menu;

	CategoryManagement(MenuManagement menu) {
		setTitle("카테고리 관리");
		this.menu = menu;
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout(FlowLayout.CENTER));
		categoryItems = menu.getCategoryItems();
		categoryCopy = new JComboBox<>(categoryItems);
		top.add(categoryCopy);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
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

	public void actionPerformed(ActionEvent ae) {
		try {
			selectedCategory = (String) categoryCopy.getSelectedItem();
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC", "root",
					"0000");
			System.out.println("DB 연결 완료.");
			Statement dbSt = con.createStatement();
			String strSql;

			if (ae.getActionCommand().equals("추가")) {
				CategoryInsert ci = new CategoryInsert(menu);
				ci.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ci.setSize(300, 300);
				ci.setLocation(900, 400);
				ci.setVisible(true);
			}

			else if (ae.getActionCommand().equals("수정")) {
				CategoryUpdate cu = new CategoryUpdate(menu, this);
				cu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				cu.setSize(300, 300);
				cu.setLocation(900, 400);
				cu.setVisible(true);
			}

			else if (ae.getActionCommand().equals("삭제")) {
				CategoryDelete cd = new CategoryDelete(menu, this);
				cd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				cd.setSize(500, 100);
				cd.setLocation(900, 400);
				cd.setVisible(true);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드에 실패했습니다.");
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}
}
