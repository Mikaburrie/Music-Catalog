package edu.miamioh.CSE201.project.ui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CatalogFrame extends JFrame {
	private static final long serialVersionUID = -556515377356781087L;
	
	private LoginPanel loginPanel;
	private MainPanel mainPanel;
	private JPanel currentPanel;

	public CatalogFrame() {
		super("Music Catalog");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//build menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		quitMenuItem.addActionListener(e -> dispose());
		fileMenu.add(quitMenuItem);
		
		ButtonGroup themeButtonGroup = new ButtonGroup();
		JRadioButtonMenuItem javaThemeMenuItem   = new JRadioButtonMenuItem("Java Theme");
		JRadioButtonMenuItem motifThemeMenuItem  = new JRadioButtonMenuItem("Motif Theme");
		JRadioButtonMenuItem systemThemeMenuItem = new JRadioButtonMenuItem("System Theme");
		themeButtonGroup.add(javaThemeMenuItem);
		themeButtonGroup.add(motifThemeMenuItem);
		themeButtonGroup.add(systemThemeMenuItem);
		javaThemeMenuItem.setSelected(true);
		
		javaThemeMenuItem.addActionListener(e -> { //action event for java theme menu button
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Could not change theme", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			SwingUtilities.updateComponentTreeUI(this);
			pack();
		});
		motifThemeMenuItem.addActionListener(e -> {//action event for motif theme menu button
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Could not change theme", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			SwingUtilities.updateComponentTreeUI(this);
			pack();
		});
		systemThemeMenuItem.addActionListener(e -> {//action event for system theme menu button
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Could not change theme", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			SwingUtilities.updateComponentTreeUI(this);
			pack();
		});
		
		viewMenu.add(javaThemeMenuItem);
		viewMenu.add(motifThemeMenuItem);
		viewMenu.add(systemThemeMenuItem);
		
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		setJMenuBar(menuBar);
		
		//create panels
		loginPanel = new LoginPanel(this);
		mainPanel  = new MainPanel(this);
		
		currentPanel = loginPanel;
		
		getContentPane().add(currentPanel, BorderLayout.CENTER);
		
		pack();
	}
	
	public void launch() {
		setVisible(true);
	}

	public void changeToMainPanel() {
		getContentPane().remove(currentPanel);
		currentPanel = mainPanel;
		getContentPane().add(currentPanel, BorderLayout.CENTER);
		SwingUtilities.updateComponentTreeUI(this);
		pack();
		revalidate();
	}

	public void changeToLoginPanel() {
		getContentPane().removeAll();
		loginPanel = null;
		loginPanel = new LoginPanel(this);
		currentPanel = loginPanel;
		getContentPane().add(currentPanel, BorderLayout.CENTER);
		SwingUtilities.updateComponentTreeUI(this);
		pack();
		revalidate();
	}
}
