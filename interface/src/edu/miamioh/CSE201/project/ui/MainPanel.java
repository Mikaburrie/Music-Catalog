package edu.miamioh.CSE201.project.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.miamioh.CSE201.project.database.*;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8779938738316416109L;
	
	private DefaultListModel<Song> songListModel;
	private JComponent songListComponent;
	
	private DefaultListModel<Playlist> playlistListModel;
	private JComponent playlistListComponent;
	
	private DefaultListModel<Song> favoriteListModel;
	private JComponent favoriteListComponent;
	
	private DefaultListModel<User> userListModel;
	private JComponent userListComponent;
	
	private JScrollPane scrollPane;

	public MainPanel(CatalogFrame frame) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(750, 800));
		
		JPanel searchBarPanel = new JPanel(new BorderLayout(3,0));
		searchBarPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		add(searchBarPanel, BorderLayout.PAGE_START);
		
		JTextField searchFeild = new JTextField();
		searchBarPanel.add(searchFeild, BorderLayout.CENTER);
		
		JPanel leftSearchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		searchBarPanel.add(leftSearchBarPanel, BorderLayout.LINE_START);
		
		JComboBox<String> searchCategoryComboBox = new JComboBox<>(new String[] {"Songs", "Playlists", "Favorites", "Users"});
		leftSearchBarPanel.add(searchCategoryComboBox);
		
		JComboBox<Object> searchOptionComboBox = new JComboBox<>();
		leftSearchBarPanel.add(searchOptionComboBox);
		
		JPanel rightSearchBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		searchBarPanel.add(rightSearchBarPanel, BorderLayout.LINE_END);
		
		JButton searchButton = new JButton("Search");
		rightSearchBarPanel.add(searchButton);
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(e -> frame.changeToLoginPanel());
		rightSearchBarPanel.add(logoutButton);
		
		songListModel = new DefaultListModel<>();
		JList<Song> tempSongListComponent = new JList<Song>(songListModel);
		tempSongListComponent.setCellRenderer(new SongListCellRenderer());
		songListComponent = tempSongListComponent;
		songListComponent.setComponentPopupMenu(new SongListPopupMenu());
		
		playlistListModel = new DefaultListModel<>();
		JList<Playlist> tempPlaylistListComponent = new JList<Playlist>(playlistListModel);
		tempPlaylistListComponent.setCellRenderer(new PlaylistListCellRenderer());
		playlistListComponent = tempPlaylistListComponent;
		playlistListComponent.setComponentPopupMenu(new PlaylistListPopupMenu(this));
		
		favoriteListModel = new DefaultListModel<>();
		JList<Song> tempFavoriteListComponent = new JList<Song>(favoriteListModel);
		tempFavoriteListComponent.setCellRenderer(new SongListCellRenderer());
		favoriteListComponent = tempFavoriteListComponent;
		favoriteListComponent.setComponentPopupMenu(new SongListPopupMenu());
		
		userListModel = new DefaultListModel<>();
		JList<User> tempUserListComponent = new JList<User>(userListModel);
		tempUserListComponent.setCellRenderer(new UserListCellRenderer());
		userListComponent = tempUserListComponent;
		userListComponent.setComponentPopupMenu(new UserListPopupMenu(this));
		
		scrollPane = new JScrollPane(songListComponent);
		add(scrollPane, BorderLayout.CENTER);
		
		
		
		
		
		
		searchCategoryComboBox.addActionListener(e -> {
			searchOptionComboBox.removeAllItems();
			Database db = Database.getInstance();
			switch((String) searchCategoryComboBox.getSelectedItem()) {
				case "Songs":
					for(Song.Column col : Song.Column.values()) {
						if(col.searchable) {
							searchOptionComboBox.addItem(col);
						}
					}
					showSongs(db.getSongs());
					break;
				case "Playlists":
					for(Playlist.Column col : Playlist.Column.values()) {
						if(col.searchable) {
							searchOptionComboBox.addItem(col);
						}
					}
					showPlaylists(db.getPlaylistBySearchString(Playlist.Column.NAME, ""));
					break;
				case "Favorites":
					for(Song.Column col : Song.Column.values()) {
						if(col.searchable) {
							searchOptionComboBox.addItem(col);
						}
					}
					showFavorites(db.getFavorites());
					break;
				case "Users":
					for(User.Column col : User.Column.values()) {
						if(col.searchable) {
							searchOptionComboBox.addItem(col);
						}
					}
					showUsers(db.getUsers());
					break;
			}
		});
		
		for(ActionListener a : searchCategoryComboBox.getActionListeners()) {
			a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "comboBoxInitialized"));
		}
		
		//search button click event
		searchButton.addActionListener(e -> {
			switch ((String)searchCategoryComboBox.getSelectedItem()) {
			case "Songs":
				showSongs(Database.getInstance().getSongBySearchString((Song.Column) searchOptionComboBox.getSelectedItem(), searchFeild.getText()));
				break;
			case "Playlists":
				showPlaylists(Database.getInstance().getPlaylistBySearchString((Playlist.Column) searchOptionComboBox.getSelectedItem(), searchFeild.getText()));
				break;
			case "Favorites":
				showFavorites(Database.getInstance().getFavoriteBySearchString((Song.Column) searchOptionComboBox.getSelectedItem(), searchFeild.getText()));
				break;
			case "Users":
				showUsers(Database.getInstance().getUserBySearchString((User.Column) searchOptionComboBox.getSelectedItem(), searchFeild.getText()));
				break;
			}
			
		});
		
	}
	
	public void showSongs(ArrayList<Song> songs) {
		songListModel.removeAllElements();
		songListModel.addElement(new Song(-1, null, null, null)); // adds entry to top to function as label
		for(Song s : songs) {
			songListModel.addElement(s);
		}
		
		scrollPane.setViewportView(songListComponent);
	}
	
	public void showPlaylists(ArrayList<Playlist> playlists) {
		playlistListModel.removeAllElements();
		playlistListModel.addElement(new Playlist(-1, -1, null, false)); // adds entry to top to function as label
		for(Playlist pl : playlists) {
			playlistListModel.addElement(pl);
		}

		scrollPane.setViewportView(playlistListComponent);
	}
	
	public void showFavorites(ArrayList<Song> favorites) {
		favoriteListModel.removeAllElements();
		favoriteListModel.addElement(new Song(-1, null, null, null)); // adds entry to top to function as label
		for(Song s : favorites) {
			favoriteListModel.addElement(s);
		}
		
		scrollPane.setViewportView(favoriteListComponent);
	}
	
	public void showUsers(ArrayList<User> users) {
		userListModel.removeAllElements();
		userListModel.addElement(new User(null, null, -1));
		for(User u : users) {
			userListModel.addElement(u);
		}
		
		scrollPane.setViewportView(userListComponent);
	}
}
