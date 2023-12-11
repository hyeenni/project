package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class CategoryInsert extends JFrame implements ActionListener {
	JLabel cg;
	JTextField category_name;
	JButton insertB, cancelB;
	MenuManagement menu;

	CategoryInsert(MenuManagement menu) {
		setTitle("카테고리 추가");
		this.menu = menu;
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout(FlowLayout.CENTER));
		cg = new JLabel("카테고리명 : ");
		category_name = new JTextField(10);
		top.add(cg);
		top.add(category_name);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		insertB = new JButton("추가");
		cancelB = new JButton("취소");
		insertB.addActionListener(this);
		cancelB.addActionListener(this);
		bottom.add(insertB);
		bottom.add(cancelB);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("취소")) {
			category_name.setText("");
		} else {
			String categoryName = category_name.getText();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
						"root", "0000");
				System.out.println("DB 연결 완료.");

				if (categoryName.isEmpty()) {
					JOptionPane.showMessageDialog(this, "모든 항목을 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String strSql = "INSERT INTO category (CategoryName) VALUES (?)";

				try (PreparedStatement ppSt = con.prepareStatement(strSql)) {
					ppSt.setString(1, categoryName);

					ppSt.executeUpdate();
				}

				JOptionPane.showMessageDialog(this, "카테고리가 추가되었습니다.", "카테고리 추가 완료", JOptionPane.INFORMATION_MESSAGE);

				category_name.setText("");
				menu.combobox();
				con.close();

				menu.showmenu();

			} catch (ClassNotFoundException e) {
				System.err.println("드라이버 로드에 실패했습니다.");
			} catch (SQLException e) {
				System.out.println("SQLException : " + e.getMessage());
			}
		}
	}
}
