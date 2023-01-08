package edu.miamioh.CSE201.project.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import edu.miamioh.CSE201.project.database.Database;
import edu.miamioh.CSE201.project.database.Playlist;

public class PlaylistListPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -1961850368146122394L;
	
	private Playlist currentPlaylist;
	private MainPanel mainPanel;
	
	public PlaylistListPopupMenu(MainPanel mainPanel) {
		super();
		this.mainPanel = mainPanel;
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
					if(obj instanceof Playlist) {
						updateMenu((Playlist) obj);
					}
				}
			}
		}
	}
	
	public void updateMenu(Playlist pl) {
		// passing null clears the menu
		removeAll();
		
		currentPlaylist = pl;
		if(currentPlaylist == null) return;
		
		Database db = Database.getInstance();
		
		add("View playlist").addActionListener(e -> {
			pl.songs = db.getPlaylistSongs(pl);
			mainPanel.showSongs(pl.songs);
		});
		
		add("Create playlist").addActionListener(e -> {
			boolean loop = true;
			while (loop) {
				loop = false;
				String name = JOptionPane.showInputDialog(this, "Enter a playlist name", "New Playlist");
				if(name != null && name.length() > 0) {
					db.createPlaylist(name, false);				
				} else if(name != null && name.length() == 0) {
					JOptionPane.showMessageDialog(this, "Name must be at least one character long", "Error", JOptionPane.ERROR_MESSAGE);
					loop = true;
				}
			}
		});
		
		if(db.curUser.ownsPlaylist(pl)) {
			add(pl.isPrivate ? "Make public" : "Make private").addActionListener(e -> {
				pl.isPrivate = pl.isPrivate ^ db.setPlaylistPrivate(pl, !pl.isPrivate);
				mainPanel.repaint();
			});
			add("Delete playlist").addActionListener(e -> db.deletePlaylist(pl));
		}
		
		add("Share");
		pack();
	}
	
}
