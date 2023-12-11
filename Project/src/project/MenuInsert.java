package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class MenuInsert extends JFrame implements ActionListener {
	JLabel cg, mn, mp, ms;
	JTextField menu_name, menu_price, menu_stock;
	Vector<String> categoryItems;
	JComboBox<String> categoryCopy;
	JButton insertB, cancelB;
	MenuManagement menu;

	MenuInsert(MenuManagement menu) {
		setTitle("메뉴 추가");
		this.menu = menu;
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new GridLayout(4, 1));

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel cg = new JLabel("카테고리 :");
		categoryItems = menu.getCategoryItems();
		categoryCopy = new JComboBox<>(categoryItems);
		p1.add(cg);
		p1.add(categoryCopy);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel mn = new JLabel("메뉴명 :");
		menu_name = new JTextField(10);
		p2.add(mn);
		p2.add(menu_name);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel mp = new JLabel("가격 :");
		menu_price = new JTextField(8);
		p3.add(mp);
		p3.add(menu_price);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel ms = new JLabel("재고 :");
		menu_stock = new JTextField(8);
		p4.add(ms);
		p4.add(menu_stock);

		top.add(p1);
		top.add(p2);
		top.add(p3);
		top.add(p4);

		bottom.setLayout(new FlowLayout());

		insertB = new JButton("추가");
		cancelB = new JButton("취소");
		cancelB.addActionListener(this);
		insertB.addActionListener(this);

		bottom.add(insertB);
		bottom.add(cancelB);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("추가")) {
			String categoryName = categoryCopy.getSelectedItem().toString();
			String menuName = menu_name.getText();
			String menuPrice = menu_price.getText();
			String menuStock = menu_stock.getText();

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
						"root", "0000");
				System.out.println("DB 연결 완료.");

				if (categoryName.isEmpty() || menuName.isEmpty() || menuPrice.isEmpty() || menuStock.isEmpty())
					JOptionPane.showMessageDialog(this, "모든 항목을 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);

				String strSql = "INSERT INTO menu (CategoryName, MenuName, MenuPrice, MenuStock) VALUES (?, ?, ?, ?)";

				try (PreparedStatement ppSt = con.prepareStatement(strSql)) {
					ppSt.setString(1, categoryName);
					ppSt.setString(2, menuName);
					try {
						ppSt.setInt(3, Integer.parseInt(menuPrice));
						ppSt.setInt(4, Integer.parseInt(menuStock));
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(this, "가격 및 재고는 숫자로 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
						return;
					}

					ppSt.executeUpdate();
				}

				JOptionPane.showMessageDialog(this, "메뉴가 추가되었습니다.", "메뉴 추가 완료", JOptionPane.INFORMATION_MESSAGE);

				categoryCopy.setSelectedIndex(0);
				menu_name.setText("");
				menu_price.setText("");
				menu_stock.setText("");

				con.close();

				menu.showmenu();

			} catch (ClassNotFoundException e) {
				System.err.println("드라이버 로드에 실패했습니다.");
			} catch (SQLException e) {
				System.out.println("SQLException : " + e.getMessage());
			}

		} else {
			categoryCopy.setSelectedIndex(0);
			menu_name.setText("");
			menu_price.setText("");
			menu_stock.setText("");
		}
	}
}
