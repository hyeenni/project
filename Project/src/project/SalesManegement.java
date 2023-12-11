package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SalesManegement extends JFrame implements ActionListener {

	JButton mm, om;

	SalesManegement() {
		Container ct = getContentPane();
		ct.setLayout(new GridLayout(2, 1));

		mm = new JButton("메뉴 관리");
		mm.addActionListener(this);
		ct.add(mm);

		om = new JButton("주문서 조회");
		om.addActionListener(this);
		ct.add(om);

		/*
		 * addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { new 메뉴창(); } }
		 */
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("메뉴 관리")) {
			MenuManagement mm = new MenuManagement();
			mm.setTitle("메뉴 관리");
			mm.setSize(470, 550);
			mm.setLocation(400, 400);
			mm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mm.setVisible(true);
			dispose();
		}
		if (ae.getActionCommand().equals("주문서 조회")) {
			OrderSheet os = new OrderSheet();
			os.setTitle("주문서 조회");
			os.setSize(930, 550);
			os.setLocation(400, 400);
			os.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			os.setVisible(true);
			dispose();
		}
	}

	public static void main(String[] args) {
		SalesManegement sm = new SalesManegement();
		sm.setTitle("영업 관리");
		sm.setSize(300, 200);
		sm.setLocation(400, 400);
		sm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sm.setVisible(true);
		;
	}
}
