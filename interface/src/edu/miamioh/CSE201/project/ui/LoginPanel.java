package edu.miamioh.CSE201.project.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.miamioh.CSE201.project.database.Database;

public class LoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -703602559208220642L;
	
	private CatalogFrame frame;

	public LoginPanel(CatalogFrame frame) {
		this.frame = frame;
		
		setPreferredSize(new Dimension(400, 250));
		setLayout(new BorderLayout());
		
		//create panels
		JPanel internalPanel = new JPanel();
		internalPanel.setLayout(new BoxLayout(internalPanel, BoxLayout.PAGE_AXIS));
		JPanel topPanel = new JPanel(new GridLayout(2, 2, 0, 5));
		JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
		bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		
		//top panel components
		JLabel unameLabel = new JLabel("Username:");
		JTextField unameFeild = new JTextField();
		
		JLabel pwordLabel = new JLabel("Password:");
		JPasswordField pwordFeild = new JPasswordField();
		
		topPanel.add(unameLabel);
		topPanel.add(unameFeild);
		topPanel.add(pwordLabel);
		topPanel.add(pwordFeild);
		
		topPanel.validate();
		
		//bottom panel components
		JButton loginButton = new JButton("Login");
		
		loginButton.addActionListener(e -> { //login button action event
			String user = unameFeild.getText();
			String pass = String.valueOf(pwordFeild.getPassword());
			System.out.println(user + " " + pass);
			if(Database.getInstance().verifyUser(user, pass)) {
				System.out.println("Valid login");
				frame.changeToMainPanel();
			}else {
				JOptionPane.showMessageDialog(this, "Invalid username or password", "Invalid Login",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		bottomPanel.add(loginButton, BorderLayout.LINE_END);
		
		//add everything to this
		internalPanel.add(Box.createVerticalGlue());
		internalPanel.add(topPanel);
		internalPanel.add(bottomPanel);
		internalPanel.add(Box.createVerticalGlue());
		
		add(internalPanel,BorderLayout.CENTER);
	}
}
