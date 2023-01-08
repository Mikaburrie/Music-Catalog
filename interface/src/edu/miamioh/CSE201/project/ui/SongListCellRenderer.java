package edu.miamioh.CSE201.project.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import edu.miamioh.CSE201.project.database.Song;

public class SongListCellRenderer extends JPanel implements ListCellRenderer<Song> {

	private static final long serialVersionUID = -234217843530948541L;
	private static final int NUM_VALUES = 3;
	
	private JLabel[] values;
	
	SongListCellRenderer() {
		setLayout(new GridLayout(1, NUM_VALUES));
		
		values = new JLabel[NUM_VALUES];
		for(int i = 0; i < NUM_VALUES; i++) {
			values[i] = new JLabel();
			values[i].setOpaque(true);
			add(values[i]);
		}
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Song> list, Song s, int index, boolean isSelected, boolean cellHasFocus) {
		Color fg = isSelected ? list.getSelectionBackground() : list.getBackground();
		Color bg = isSelected ? list.getSelectionForeground() : list.getForeground();
		
		if(index == 0) {
			fg = Color.GRAY;
			bg = Color.WHITE;
			values[0].setText(Song.Column.TITLE.name);
			values[1].setText(Song.Column.ARTIST.name);
			values[2].setText(Song.Column.GENRE.name);
		} else {
			values[0].setText(s.title);
			values[1].setText(s.artist);
			values[2].setText(s.genre);			
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
