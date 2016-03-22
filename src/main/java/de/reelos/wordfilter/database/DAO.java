package de.reelos.wordfilter.database;

import java.io.BufferedReader;
import java.io.InputStream;
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
	private List<BlackWord> swearList;
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
				InputStream fis = DAO.class.getResourceAsStream("/Create.SQL");
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
				String query = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					query += line + "\n";
				}
				reader.close();
				String[] queries = query.split(";");
				for (String q : queries) {
					if (!q.trim().isEmpty()) {
						con.createStatement().executeUpdate(q);
					}
				}
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
				swearList.add(new BlackWord(swearing.getString("Added_By"),swearing.getString("Word"),swearing.getInt("For_All")));
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

	public List<BlackWord> getBadWords() {
		return swearList;
	}

	public void reloadList() {
		loadList();
	}

	/**
	 * Adds a Specific Word to the Blacklist
	 * 
	 * @param playername
	 *            Name of the Player which added the SwearWord.
	 * @param word
	 *            The Word which should be added to the blacklist.
	 * @param flag
	 *            for if all or just some special snowflakes should be unable to
	 *            use this word.
	 * @return either -1 if an error ocoures, 0 if nothing changed or 1 if the
	 *         word was successfully added.
	 */
	public int AddWord(String playername, String word, int flag) {
		int ret = -1;
		try {
			openDB();
			PreparedStatement ps = con.prepareStatement("INSERT INTO BadWords (Added_By, Word, For_All VALUES (?,?,?)");
			ps.setString(1, playername);
			ps.setString(2, word);
			ps.setInt(3, flag);
			ret = ps.executeUpdate();
			closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reloadList();
		return ret;
	}
}
