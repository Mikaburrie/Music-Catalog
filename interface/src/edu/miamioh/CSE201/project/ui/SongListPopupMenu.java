package edu.miamioh.CSE201.project.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import edu.miamioh.CSE201.project.database.Database;
import edu.miamioh.CSE201.project.database.Playlist;
import edu.miamioh.CSE201.project.database.Song;

public class SongListPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -2502264330240482999L;
	private Song currentSong;
	private JMenu playlistMenu;

	public SongListPopupMenu() {
		super();
		updateMenu(null);
	}
	
	@Override
	public void show(Component c, int x, int y) {
		if (c instanceof JList) {
			JList<?> list = (JList<?>) c;
			Point pt = new Point(x, y);
			int index = list.locationToIndex(pt);
			Rectangle r = list.getCellBounds(index, index);
			if (r != null && r.contains(pt) && index != 0) {
				super.show(c, x, y);
				list.setSelectedIndex(index);
				ListModel<?> model = list.getModel();
				if(model instanceof DefaultListModel<?>) {
					DefaultListModel<?> listModel = (DefaultListModel<?>) list.getModel();
					Object obj = listModel.get(index);
					if(obj instanceof Song) {
						updateMenu((Song) obj);
					}
				}
			}
		}
	}
	
	public void updateMenu(Song s) {
		// passing null clears the menu
		removeAll();
		currentSong = s;
		if(currentSong == null) return;
		
		Database db = Database.getInstance();
		
		playlistMenu = new JMenu("Add to playlist");
		ArrayList<Playlist> playlists = Database.getInstance().getUserPlaylists();

		for(Playlist pl : playlists) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(pl.name);
			playlistMenu.add(item).addActionListener(e -> {
				if(!item.isSelected()) {
					boolean success = db.removeSongFromPlaylist(pl, s);
					System.out.printf("%s %s from %s%n", success ? "Removed" : "Failed removing", s.title, pl.name);
					item.setSelected(!success);
				} else {
					boolean success = db.addSongToPlaylist(pl, s);
					System.out.printf("%s %s to %s%n", success ? "Added" : "Failed adding", s.title, pl.name);
					item.setSelected(success);
				}
			});;
			item.setSelected(db.isSongInPlaylist(pl, s));
		}
		
		playlistMenu.addSeparator();
		playlistMenu.add("+ Add to new playlist").addActionListener(e -> {
			boolean loop = true;
			while (loop) {
				loop = false;
				String name = JOptionPane.showInputDialog(this, "Enter a playlist name", "New Playlist");
				if(name != null && name.length() > 0) {
					Playlist pl = db.createPlaylist(name, false);
					db.addSongToPlaylist(pl, s);				
				} else if(name != null && name.length() == 0) {
					JOptionPane.showMessageDialog(this, "Name must be at least one character long", "Error", JOptionPane.ERROR_MESSAGE);
					loop = true;
				}
			}
		});
		
		add(playlistMenu);
		
		boolean isFavorite = db.isFavorite(s);
		add(isFavorite ? "Unfavorite" : "Add to favorites").addActionListener(e -> {
			if(isFavorite) {
				boolean success = db.removeFavorite(s);
				System.out.printf("%s %s from favorites", success ? "Removed" : "Failed removing", s.title);
			} else {
				boolean success = db.addFavorite(s);
				System.out.printf("%s %s from favorites", success ? "Added" : "Failed adding", s.title);
			}
		});
		
		add("Share");
		
		pack();
	}
	
}
