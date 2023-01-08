package edu.miamioh.CSE201.project.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.miamioh.CSE201.project.database.Playlist;

public class PlaylistListCellRenderer extends JPanel implements ListCellRenderer<Playlist> {

	private static final long serialVersionUID = -4956570176601140667L;
	private static final int NUM_VALUES = 4;
	
	private JLabel[] values;
	
	PlaylistListCellRenderer() {
		setLayout(new GridLayout(1, NUM_VALUES));
		
		values = new JLabel[NUM_VALUES];
		for(int i = 0; i < NUM_VALUES; i++) {
			values[i] = new JLabel();
			values[i].setOpaque(true);
			add(values[i]);
		}
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Playlist> list, Playlist pl, int index, boolean isSelected, boolean cellHasFocus) {
		Color fg = isSelected ? list.getSelectionBackground() : list.getBackground();
		Color bg = isSelected ? list.getSelectionForeground() : list.getForeground();
		
		if(index == 0) {
			fg = Color.GRAY;
			bg = Color.WHITE;
			values[0].setText(Playlist.Column.NAME.name);
			values[1].setText("User");
			values[2].setText("Visibility");
			values[3].setText("Song Count");
		} else {
			values[0].setText(pl.name);
			values[1].setText(pl.user.userName);
			values[2].setText(pl.isPrivate ? "Private" : "Public");
			values[3].setText(Integer.toString(pl.songs.size()));
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
