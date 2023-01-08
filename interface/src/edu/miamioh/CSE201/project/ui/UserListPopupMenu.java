package edu.miamioh.CSE201.project.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import edu.miamioh.CSE201.project.database.Database;
import edu.miamioh.CSE201.project.database.User;

public class UserListPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 515021679040801042L;
	
	private User currentUser;
	private MainPanel mainPanel;
	
	public UserListPopupMenu(MainPanel mainPanel) {
		super();
		this.mainPanel = mainPanel;
		
	}
	
	@Override
	public void show(Component c, int x, int y) {
		if (c instanceof JList) {
			JList<?> list = (JList<?>) c;
			Point pt = new Point(x, y);
			int index = list.locationToIndex(pt);
			Rectangle r = list.getCellBounds(index, index);
			if (r != null && r.contains(pt)) {
				super.show(c, x, y);
				list.setSelectedIndex(index);
				ListModel<?> model = list.getModel();
				if(model instanceof DefaultListModel<?>) {
					DefaultListModel<?> listModel = (DefaultListModel<?>) list.getModel();
					Object obj = listModel.get(index);
					if(obj instanceof User) {
						updateMenu((User) obj);
					}
				}
			}
		}
	}
	
	public void updateMenu(User u) {
		// passing null clears the menu
		removeAll();
		
		currentUser = u;
		if(currentUser == null) return;
		
		Database db = Database.getInstance();
		
		add("View playlists").addActionListener(e -> mainPanel.showPlaylists(db.getUserPlaylists(u)));
		add("View favorites").addActionListener(e -> mainPanel.showSongs(db.getUserFavorites(u)));
		
		add("Share");
		pack();
	}
	
}
