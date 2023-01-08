package edu.miamioh.CSE201.project.database;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Database {
	
	private static Database instance;
	
	Connection dbc;
	
	private Database() {}
	
	public User curUser;

	public static Database getInstance() {
		if(instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public boolean connectTo(String url) {
		try {
			dbc = DriverManager.getConnection(url);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void executeQueryFile(String filePath) {
		ArrayList<String> lines = null;

		try {
			lines = (ArrayList<String>) Files.readAllLines(Paths.get(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		for(String s : lines)
			builder.append(s);
		builder.trimToSize();
		
		try {
			Statement statement = dbc.createStatement();
			
			String[] queries = builder.toString().split(";");
			for(String query : queries) {
				statement.addBatch(query);
			}
		
			statement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean verifyUser(String user, String pass) {
		try {
			PreparedStatement ps = dbc.prepareStatement("SELECT * FROM Users WHERE Username = ? AND Password = ?");
			ps.setString(1, user);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			
			curUser = null;
			while(rs.next()) {
				curUser = new User(user, pass, rs.getInt(1));
			}
			
			return curUser != null;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Song> getSongs() {
		try {
			PreparedStatement ps = dbc.prepareStatement("SELECT * FROM Songs");
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Song> getSortedSongs(Song.Column column) {
		return getSortedSongs(new Song.Column[] {column});
	}

	public ArrayList<Song> getSortedSongs(Song.Column[] sortOrder) {
		try {
			String order = Arrays.toString(sortOrder);
			String queryStr = "SELECT * FROM Songs ORDER BY " + order.substring(1, order.length() - 1);
			PreparedStatement ps = dbc.prepareStatement(queryStr);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Song> getSongBySearchString(Song.Column column, String search){
		try {
			String query = "SELECT * FROM Songs WHERE " + column.name + " LIKE ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setString(1, "%" + search + "%");
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}

	public ArrayList<Song> getPlaylist(String name) {
		String queryStr = "SELECT Songs.* FROM Playlists " +
						  "INNER JOIN PlaylistSongs ON PlaylistSongs.PlaylistID = Playlists.PlaylistID " +
						  "INNER JOIN Songs ON Songs.SongID = Playlists.SongID " + 
						  "WHERE Playlists.UserID = " + curUser.uid;
		if (!name.equals("")) {
			queryStr += " Playlists.Name = " + name;
		}
		try {
			PreparedStatement ps = dbc.prepareStatement(queryStr);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Song> getPlaylistSongs(Playlist pl) {
		try {
			String query = "SELECT Songs.* FROM PlaylistSongs " +
					  		"INNER JOIN Songs ON Songs.SongID = PlaylistSongs.SongID " + 
					  		"WHERE PlaylistSongs.PlaylistID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, pl.playlistid);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Playlist> getUserPlaylists() {
		try {
			String query = "SELECT * FROM Playlists pl JOIN Users u ON u.UserID = pl.UserID WHERE pl.UserID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, curUser == null ? 0 : curUser.uid);
			ResultSet rs = ps.executeQuery();
			ArrayList<Playlist> playlists = Playlist.resultSetToPlaylistArrayList(rs);
			for(Playlist playlist : playlists) {
				playlist.songs = getPlaylistSongs(playlist);
			}
			return playlists;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Playlist>();
		}
	}
	
	public ArrayList<Playlist> getUserPlaylists(User u) {
		try {
			String query = "SELECT * FROM Playlists pl JOIN Users u ON u.UserID = pl.UserID WHERE pl.UserID = ? AND pl.Private = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, u.uid);
			ps.setInt(2, u.uid == curUser.uid ? 1 : 0);
			ResultSet rs = ps.executeQuery();
			ArrayList<Playlist> playlists = Playlist.resultSetToPlaylistArrayList(rs);
			for(Playlist playlist : playlists) {
				playlist.songs = getPlaylistSongs(playlist);
			}
			return playlists;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Playlist>();
		}
	}
	
	public ArrayList<Playlist> getPlaylistBySearchString(Playlist.Column column, String search) {
		try {
			String query = "SELECT * FROM Playlists pl JOIN Users u ON u.UserID = pl.UserID WHERE " + column.name + " LIKE ? AND (pl.UserID = ? OR Private = 0)";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setString(1, "%" + search + "%");
			ps.setInt(2, curUser.uid);
			ResultSet rs = ps.executeQuery();
			ArrayList<Playlist> playlists = Playlist.resultSetToPlaylistArrayList(rs);
			for(Playlist playlist : playlists) {
				playlist.songs = getPlaylistSongs(playlist);
			}
			return playlists;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Playlist>();
		}
	}
	
	public Playlist createPlaylist(String name, boolean isPrivate) {
		try {
			String query = "INSERT INTO Playlists (UserID, Name, Private) VALUES (?, ?, ?) RETURNING *, (SELECT Username FROM Users WHERE UserID = Playlists.UserID)";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, curUser.uid);
			ps.setString(2, name);
			ps.setInt(3, isPrivate ? 1 : 0);
			ResultSet rs = ps.executeQuery();
			return Playlist.resultSetToPlaylistArrayList(rs).get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean deletePlaylist(Playlist pl) {
		try {
			pl.songs = getPlaylistSongs(pl);
			String query = "DELETE FROM Playlists WHERE PlaylistID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, pl.playlistid);
			if(ps.executeUpdate() == 1) {
				for(Song s : pl.songs) {
					removeSongFromPlaylist(pl, s);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addSongToPlaylist(Playlist pl, Song s) {
		try {
			String query = "INSERT INTO PlaylistSongs (PlaylistID, SongID, DateAdded) VALUES (?, ?, ?)";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, pl.playlistid);
			ps.setInt(2, s.id);
			ps.setLong(3, Instant.now().getEpochSecond());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeSongFromPlaylist(Playlist pl, Song s) {
		try {
			String query = "DELETE FROM PlaylistSongs WHERE PlaylistID = ? AND SongID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, pl.playlistid);
			ps.setInt(2, s.id);
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isSongInPlaylist(Playlist pl, Song s) {
		try {
			String query = "SELECT COUNT(*) AS InPlaylist FROM PlaylistSongs WHERE PlaylistID = ? AND SongID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, pl.playlistid);
			ps.setInt(2, s.id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setPlaylistPrivate(Playlist pl, boolean isPrivate) {
		try {
			String query = "UPDATE Playlists SET Private = ? WHERE PlaylistID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			System.out.println(isPrivate ? 1 : 0);
			ps.setInt(1, isPrivate ? 1 : 0);
			ps.setInt(2, pl.playlistid);
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Song> getFavorites() {
		try {
			String queryStr = "SELECT s.* FROM UserFavorites uf INNER JOIN Songs s ON s.SongID = uf.SongID WHERE uf.UserID = ?";
			PreparedStatement ps = dbc.prepareStatement(queryStr);
			ps.setInt(1, curUser.uid);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Song> getUserFavorites(User u) {
		try {
			String query = "SELECT * FROM Songs s JOIN UserFavorites uf ON uf.SongID = s.SongID WHERE UserID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, u.uid);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	public ArrayList<Song> getFavoriteBySearchString(Song.Column column, String search) {
		try {
			String query = "SELECT * FROM Songs s JOIN UserFavorites uf ON uf.SongID = s.SongID WHERE " + column.name + " LIKE ? AND UserID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setString(1, "%" + search + "%");
			ps.setInt(2, curUser.uid);
			ResultSet rs = ps.executeQuery();
			return Song.resultSetToSongArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Song>();
		}
	}
	
	
	public boolean addFavorite(Song s) {
		try {
			String query = "INSERT INTO UserFavorites (UserID, SongID, DateAdded) VALUES (?, ?, ?)";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, curUser.uid);
			ps.setInt(2, s.id);
			ps.setLong(3, Instant.now().getEpochSecond());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean removeFavorite(Song s) {
		try {
			String query = "DELETE FROM UserFavorites WHERE UserID = ? AND SongID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, curUser.uid);
			ps.setInt(2, s.id);
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean isFavorite(Song s) {
		try {
			String query = "SELECT COUNT(*) AS InFavorites FROM UserFavorites WHERE UserID = ? AND SongID = ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setInt(1, curUser.uid);
			ps.setInt(2, s.id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1) == 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<User> getUsers() {
		String queryStr = "SELECT UserID, Username, '' AS Password FROM Users";
		try {
			PreparedStatement ps = dbc.prepareStatement(queryStr);
			ResultSet rs = ps.executeQuery();
			return User.resultSetToUserArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<User>();
		}
	}
	
	public ArrayList<User> getUserBySearchString(User.Column column, String search) {
		try {
			String query = "SELECT UserID, Username, '' AS Password FROM Users WHERE " + column.name + " LIKE ?";
			PreparedStatement ps = dbc.prepareStatement(query);
			ps.setString(1, "%" + search + "%");
			ResultSet rs = ps.executeQuery();
			return User.resultSetToUserArrayList(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<User>();
		}
	}
	
}
