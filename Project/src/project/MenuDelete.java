package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class MenuDelete extends JFrame implements ActionListener {
	JLabel message;
	JButton deleteB, cancelB;
	MenuManagement menu;

	MenuDelete(MenuManagement menu) {
		setTitle("메뉴 삭제");
		this.menu = menu;
		Container ct = getContentPane();
		ct.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		ct.add(top, BorderLayout.CENTER);
		ct.add(bottom, BorderLayout.SOUTH);

		top.setLayout(new FlowLayout(FlowLayout.CENTER));
		message = new JLabel("정말 삭제하시겠습니까?");
		top.add(message);

		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		deleteB = new JButton("삭제");
		cancelB = new JButton("취소");
		deleteB.addActionListener(this);
		cancelB.addActionListener(this);
		bottom.add(deleteB);
		bottom.add(cancelB);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("취소"))
			dispose();
		else {
			String menuCode = menu.selectedMenuCode;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/menu?serverTimezone=UTC",
						"root", "0000");
				System.out.println("DB 연결 완료.");

				String strSql = "DELETE FROM menu WHERE MenuCode = '" + menuCode + "'";

				try (Statement st = con.createStatement()) {
					st.executeUpdate(strSql);
				}

				JOptionPane.showMessageDialog(this, "메뉴가 삭제되었습니다.", "메뉴 삭제 완료", JOptionPane.INFORMATION_MESSAGE);

				con.close();
				menu.row = -1;
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
