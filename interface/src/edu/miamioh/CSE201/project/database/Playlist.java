package edu.miamioh.CSE201.project.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Playlist {
	
    protected int playlistid;
    protected int userid;
    public String name;
    public boolean isPrivate;
    public User user;
    public ArrayList<Song> songs;
    
    public static enum Column {
		PLAYLISTID	("PlaylistID", 1, false),
		USERID		("UserID", 2, false),
		NAME		("Name", 3, true),
		PRIVATE		("Private", 4, false),
		USERNAME	("Username", -1, true);
		
		public final String name;
		protected final int colNum;
		public final boolean searchable;
		
		Column(String name, int colNum, boolean searchable) {
			this.name = name;
			this.colNum = colNum;
			this.searchable = searchable;
		}
		
		public String toString() {
			return name;
		}
	}

    public Playlist(int id, int userid, String name, boolean isPrivate, User user) {
    	this.playlistid = id;
    	this.userid = userid;
        this.name = name;
        this.isPrivate = isPrivate;
        this.user = user;
    }
    
    public Playlist(int id, int userid, String name, boolean isPrivate) {
    	this(id, userid, name, isPrivate, null);
    }
    
    public String toString() {
		return String.format("%nname: %-31s isprivate: %-26b size: %-32d user: %-30s", name, isPrivate, songs != null ? songs.size() : 0, user == null ? null : user.userName);
	}
    
    protected static ArrayList<Playlist> resultSetToPlaylistArrayList(ResultSet rs) {
		ArrayList<Playlist> playlists = new ArrayList<>();
		
		try {
			while(rs.next()) {
				playlists.add(new Playlist(
						rs.getInt(Column.PLAYLISTID.colNum),
						rs.getInt(Column.USERID.colNum),
						rs.getString(Column.NAME.colNum),
						rs.getBoolean(Column.PRIVATE.colNum),
						new User(
							rs.getString(5), // username
							"", // dont assign passwords
							rs.getInt(Column.USERID.colNum)
						)
					));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return playlists;
	}
    
}