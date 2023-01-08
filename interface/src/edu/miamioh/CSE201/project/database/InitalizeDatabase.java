package edu.miamioh.CSE201.project.database;

import java.util.ArrayList;

public class InitalizeDatabase {

	public static void main(String[] args) {
		Database db = Database.getInstance();
		db.connectTo("jdbc:sqlite:database/database.db");
		db.executeQueryFile("database/create_database.sql");
	}
	
	public static void testDbQueries() {
		Database db = Database.getInstance();
		System.out.println(db.verifyUser("", "")); // false
		System.out.println(db.verifyUser("user_not_found", "NULL")); // false
		System.out.println(db.verifyUser("admin", "password123")); // true
		System.out.println(db.verifyUser("burmesmt", "testpass123")); // true
		System.out.println(db.getSortedSongs(new Song.Column[] {Song.Column.ARTIST, Song.Column.TITLE})); // dumps all songs to console
		System.out.println(db.getSortedSongs(Song.Column.GENRE));
		System.out.println(db.getSongBySearchString(Song.Column.GENRE, "rock"));
		ArrayList<Playlist> pls = db.getUserPlaylists();
		System.out.println(pls); // TestPlaylist, 3 songs
		System.out.println(pls.get(0).songs); // Hold On, Lovefool, Step Aside Playa
		System.out.println(db.addSongToPlaylist(new Playlist(1, 2, null, false), new Song(7, null, null, null)));
		System.out.println(db.addSongToPlaylist(new Playlist(1, 2, null, false), new Song(8, null, null, null)));
		System.out.println(db.addSongToPlaylist(new Playlist(2, 1, null, true), new Song(7, null, null, null)));
		System.out.println(db.isSongInPlaylist(new Playlist(2, 1, null, true), new Song(7, null, null, null)));
		System.out.println(db.isSongInPlaylist(new Playlist(20, 1, null, true), new Song(7, null, null, null)));
		System.out.println(db.removeSongFromPlaylist(new Playlist(2, 1, null, true), new Song(7, null, null, null)));
		System.out.println(db.createPlaylist("test", false));
	}
	
}
