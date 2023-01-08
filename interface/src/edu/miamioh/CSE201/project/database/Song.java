package edu.miamioh.CSE201.project.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Song {
	
	protected final int id;
	public final String title;
	public final String genre;
	public final String artist;
	
	public static enum Column {
		SONGID	("SongID", 1, false),
		TITLE	("Title", 3, true),
		GENRE	("Genre", 2, true),
		ARTIST	("Artist", 4, true);
		
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
	
	public Song(int id, String genre, String title, String artist) {
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.artist = artist;
	}
	
	public String toString() {
		return String.format("%ntitle: %-30s genre: %-30s artist: %-30s", title, genre, artist);
	}
	
	protected static ArrayList<Song> resultSetToSongArrayList(ResultSet rs) {
		ArrayList<Song> songs = new ArrayList<>();
		
		try {
			while(rs.next()) {
				songs.add(new Song(
						rs.getInt(Column.SONGID.colNum),
						rs.getString(Column.GENRE.colNum),
						rs.getString(Column.TITLE.colNum),
						rs.getString(Column.ARTIST.colNum)
					));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return songs;
	}
	
}
