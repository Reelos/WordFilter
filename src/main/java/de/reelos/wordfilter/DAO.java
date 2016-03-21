package de.reelos.wordfilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Connection to MySQL Database. I'm using a Singleton
 * for this cause there shouldn't be more than one instance of this class
 * 
 * @author Reelos
 */
public class DAO {
	public static final DAO instance = new DAO();
	private Connection con;
	private List<String> swearList;
	private final String baseurl = "jdbc:mysql://localhost:3306/";
	private final String database = "WordFilter";

	private DAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(baseurl, "root", "");
			ResultSet catalogs = con.getMetaData().getCatalogs();
			boolean found = false;
			while (catalogs.next()) {
				if (catalogs.getString(1).matches(database)) {
					found = true;
					break;
				}
			}
			if (!found) {
				File sql = new File(getClass().getResource("Create.SQL").getFile());
				FileInputStream fis = new FileInputStream(sql);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
				String query = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					query += line;
				}
				reader.close();
				con.createStatement().executeUpdate(query);
			}
			con.close();
			loadList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens Connection
	 */
	private void openDB() throws SQLException {
		closeDB();
		con = DriverManager.getConnection(baseurl + database, "root", "");
	}

	private void loadList() {
		swearList = new ArrayList<>();
		try {
			openDB();
			ResultSet swearing = con.createStatement().executeQuery("SELECT * FROM BadWords");
			while (swearing.next()) {
				swearList.add(swearing.getString("Word"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes Connection
	 */
	private void closeDB() throws SQLException {
		if (!con.isClosed()) {
			con.close();
		}
	}

	public List<String> getBadWords() {
		return swearList;
	}

	public void reloadList() {
		loadList();
	}

	public void AddWord(String playername, String word, boolean flag) {
		try {
			openDB();
			PreparedStatement ps = con.prepareStatement("INSERT INTO BadWords (Added_By, Word, For_All VALUES (?,?,?)");
			ps.setString(1, playername);
			ps.setString(2, word);
			ps.setBoolean(3, flag);
			ps.executeUpdate();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reloadList();
	}
}
