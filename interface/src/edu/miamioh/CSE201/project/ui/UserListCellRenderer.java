package edu.miamioh.CSE201.project.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.miamioh.CSE201.project.database.User;

public class UserListCellRenderer extends JPanel implements ListCellRenderer<User> {

	private static final long serialVersionUID = -5586129782299324762L;
	private static final int NUM_VALUES = 1;
	
	private JLabel[] values;
	
	UserListCellRenderer() {
		setLayout(new GridLayout(1, NUM_VALUES));
		
		values = new JLabel[NUM_VALUES];
		for(int i = 0; i < NUM_VALUES; i++) {
			values[i] = new JLabel();
			values[i].setOpaque(true);
			add(values[i]);
		}
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends User> list, User u, int index, boolean isSelected, boolean cellHasFocus) {
		Color fg = isSelected ? list.getSelectionBackground() : list.getBackground();
		Color bg = isSelected ? list.getSelectionForeground() : list.getForeground();
		
		if(index == 0) {
			fg = Color.GRAY;
			bg = Color.WHITE;
			values[0].setText("Name");
		} else {
			values[0].setText(u.userName);
		}
				
		for(JLabel value : values) {
			value.setBackground(fg);
			value.setForeground(bg);
		}
		
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		return this;
	}

}
