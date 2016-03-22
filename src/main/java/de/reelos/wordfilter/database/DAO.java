package de.reelos.wordfilter.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Connection to MySQL Database. I'm using a Singleton
 * for this cause there shouldn't be more than one instance of this class.
 * 
 * @author Reelos
 */
public class DAO {
	public static final DAO instance = new DAO();
	private Connection con;
	private List<BlackWord> swearList;
	private final String baseurl;
	private final String database;
	private final String username;
	private final String password;

	private DAO() {
		//Creation of Ini File to for Database connection Settings. Also with defaults.
		File file = new File("./Plugins/WordFilter.ini");
		String db = "WordFilter";
		String server = "localhost:3306";
		String user = "root";
		String pass = "";
		if (!file.exists()) {
			try {
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				writer.write("Database="+db);
				writer.newLine();
				writer.write("Server="+server);
				writer.newLine();
				writer.write("User="+user);
				writer.newLine();
				writer.write("Password"+pass);
				writer.newLine();
				writer.close();
			} catch (IOException e) {
				System.out.println("Ini Creation Failed.");
				e.printStackTrace();
			}
		}
		//Reading of Ini File to get Value settings
		try {
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if(line.contains("Database=")){
					db=line.substring(line.indexOf("=")+1).trim();
				}
				if(line.contains("Server=")) {
					server=line.substring(line.indexOf("=")+1).trim();
				}
				if(line.contains("Password=")){
					pass=line.substring(line.indexOf("=")+1).trim();
				}
				if(line.contains("User=")) {
					user=line.substring(line.indexOf("=")+1).trim();
				}
			}
			reader.close();
		} catch (IOException e1) {
			System.out.println("IniReading Failed");
			e1.printStackTrace();
		}
		//Set Values into local Final vars
		database = db;
		baseurl = "jdbc:mysql://"+server+"/";
		username = user;
		password = pass;

		//Opening first connection to check for existing database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(baseurl, username, password);
			ResultSet catalogs = con.getMetaData().getCatalogs();
			boolean found = false;
			while (catalogs.next()) {
				if (catalogs.getString(1).equalsIgnoreCase(database)) {
					found = true;
					break;
				}
			}
			//If no database was found, it will be created and filled with some basic words
			//This script can be found in the resource folder
			if (!found) {
				con.createStatement().executeUpdate("CREATE SCHEMA IF NOT EXISTS "+database);
				System.out.println("[WordFilter] Database '"+database+"' created");
				con.createStatement().executeUpdate("USE "+database);
				InputStream fis = DAO.class.getResourceAsStream("/Create.SQL");
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
				String query = "";
				String line = "";
				while ((line = reader.readLine()) != null) {
					query += line + "\n";
				}
				reader.close();
				//Executing every Query provided by Create.SQL
				String[] queries = query.split(";");
				for (String q : queries) {
					if (!q.trim().isEmpty()) {
						con.createStatement().executeUpdate(q);
					}
				}
				System.out.println("[WordFilter] 'Create.SQL' executed.");
			}
			con.close();
			loadList();
		} catch (Exception e) {
			System.out.println("Database Connection Failed.");
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

	/**
	 * Loads the blacklist into internal memory.
	 */
	private void loadList() {
		swearList = new ArrayList<>();
		try {
			openDB();
			ResultSet swearing = con.createStatement().executeQuery("SELECT * FROM BadWords");
			while (swearing.next()) {
				swearList.add(new BlackWord(swearing.getString("Added_By"), swearing.getString("Word"),
						swearing.getInt("For_All")));
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

	/**
	 * 
	 * @return List of BlackWord Class Objects
	 */
	public List<BlackWord> getBadWords() {
		return swearList;
	}

	/**
	 * override of private loadList()
	 */
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
	public int addWord(String playername, String word, int flag) {
		int ret = -1;
		try {
			openDB();
			PreparedStatement ps = con.prepareStatement("INSERT INTO BadWords (Added_By, Word, For_All) VALUES (?,?,?)");
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
	
	/**
	 * Removes a Word from the blacklist table.
	 * @param word String that should be removed from blacklist table.
	 * @return either -1 on error, 0 on no change or 1 on success.
	 */
	public int removeWord(String word) {
		int ret = -1;
		try {
			openDB();
			PreparedStatement ps = con.prepareStatement("DELETE FROM BadWords WHERE word LIKE ?;");
			ps.setString(1, word);
			ret = ps.executeUpdate();
			closeDB();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * Set the Flag of a blacklisted word.
	 * @param word String of blacklisted word to set flag
	 * @param flag int to set flag
	 * @return -1 on any error, 0 if word not found, 1 on success
	 */
	public int changeWordFlag(String word, int flag){
		int ret = -1;
		try{
			openDB();
			PreparedStatement ps = con.prepareStatement("UPDATE BadWords SET For_All = ? WHERE word LIKE ?");
			ps.setInt(1, flag);
			ps.setString(2, word);
			ret = ps.executeUpdate();
			closeDB();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return ret;
	}
}
