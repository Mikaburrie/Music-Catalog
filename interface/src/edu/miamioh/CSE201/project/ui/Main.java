package edu.miamioh.CSE201.project.ui;

import edu.miamioh.CSE201.project.database.Database;

public class Main {

	public static void main(String[] args) {
		if (Database.getInstance().connectTo("jdbc:sqlite:database/database.db")) {
			System.out.println("Connected to DB");
		}
		
		CatalogFrame frame = new CatalogFrame();
		frame.launch();
	}

}
