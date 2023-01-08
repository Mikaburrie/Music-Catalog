package edu.miamioh.CSE201.project.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    public final String userName;
    public final String password;
    public final int uid;
    
    public static enum Column {
		USERID		("UserID", 1, false),
		USERNAME	("Username", 2, true),
		PASSWORD	("Password", 3, false);
		
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

    public User(String name, String pwd, int id) {
        this.userName = name;
        this.password = pwd;
        this.uid = id;
    }

    public String toString() {
        return String.format("%nUser Name: %-30s Password: %-30s ID: %-30s", userName, password, uid);
    }
    
    public boolean ownsPlaylist(Playlist pl) {
    	return uid == pl.userid;
    }
    
    protected static ArrayList<User> resultSetToUserArrayList(ResultSet rs) {
		ArrayList<User> users = new ArrayList<>();
		
		try {
			while(rs.next()) {
				users.add(new User(
						rs.getString(Column.USERNAME.colNum),
						rs.getString(Column.PASSWORD.colNum),
						rs.getInt(Column.USERID.colNum)
					));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
    
}
