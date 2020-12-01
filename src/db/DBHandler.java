package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import objects.CartUser;

public class DBHandler {

	private String dbName;
	private String dbUsername;
	private String dbPassword;
	private String dbTableUser;
	private String dbTableCart;
	private String key;
	private Connection dbConnection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMeatData;

	private String dbServerURL;
	private String dbURL;
	private String selectQuery;
	private String dbDriver;
	private String serverTimeZone = "?serverTimezone=";
	private String[] acceptedImageSuffixes;
	private ResourceBundle resourceBundle;

	private static String resourceDir, tempImageDir, tempImageDirAbsolute;
	private File tempImageDirAbsoluteFileObj;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");

//	public DBHandler(String dbName, String dbUsername, String dbPassword) throws Exception {

//		this.dbName = dbName;
//		this.dbUsername = dbUsername;
//		this.dbPassword = dbPassword;

		
	public DBHandler() throws Exception {
		try {
		
			resourceBundle = ResourceBundle.getBundle("db.settings", new Locale(""));
			
			dbUsername 	= resourceBundle.getString("db_username").trim();
			dbPassword 	= resourceBundle.getString("db_password").trim();
			dbName 	   	= resourceBundle.getString("db_name").trim();
			dbTableUser = resourceBundle.getString("db_table_users").trim();
			dbTableCart = resourceBundle.getString("db_table_carts").trim();
			key			= resourceBundle.getString("encryption_key");
			
			dbServerURL = resourceBundle.getString("db_server_url").trim();

			serverTimeZone = serverTimeZone + resourceBundle.getString("server_time_zone").trim();

			dbDriver = resourceBundle.getString("db_driver").trim();
			selectQuery = resourceBundle.getString("select_query") + " ";

			// Here we specify the database full path as well as the time zone in which the
			// query is executed.
		
			dbURL = dbServerURL + "/" + dbName + serverTimeZone;

			acceptedImageSuffixes = resourceBundle.getString("accepted_image_suffixes").trim().split(" ");

		} catch (Exception e) {
			throw new Exception();
		}

	}

	public static void setResourceDir(String resourceDir) {
		DBHandler.resourceDir = resourceDir;
	}

	public static void setTempImageDirAbsolute(String tempImageDirAbsolute) {
		DBHandler.tempImageDirAbsolute = tempImageDirAbsolute;

	}

	public static void setTempImageDir(String tempImageDir) {
		DBHandler.tempImageDir = tempImageDir + File.separator;

	}

	private void openConnection() {

		// For mySQL database the above code would look like this:
		try {

			Class.forName(dbDriver);
			System.out.println("Database conn open");
			// Here we create connection to the database
			dbConnection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	private String closeConnection() {

		try {
			System.out.println("Database conn close");
			if (statement != null)
				statement.close();
			if (dbConnection != null)
				dbConnection.close();
		} catch (SQLException sqlexc) {
			return "Exception occurred while closing database connection!";
		}

		return "";

	}

	public String getDataTable() {
		openConnection();

		String query = selectQuery + this.dbTableUser;
		StringBuilder resultString = new StringBuilder();

		
		// Here we create the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			// object
			resultSet = statement.executeQuery(query);

			// Here we get the metadata of the query results
			resultSetMeatData = resultSet.getMetaData();

			while (resultSet.next()) {
				
				System.out.println(resultSet.getObject(1).toString());
				System.out.println(resultSet.getObject(2).toString());
				
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println(closeConnection());
		}

		return resultString.toString();
	}
	
	public String userLogin(String username, String password ) {
		openConnection();
		
		// Here we create the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			
			String getUserQuery = 
	
"SELECT id,username, CAST(AES_DECRYPT(password,'" + key + "') AS CHAR) FROM "+ this.getDbTableUser();
			
			// object
			resultSet = statement.executeQuery(getUserQuery);

			// Here we get the metadata of the query results

			while (resultSet.next()) {
				System.out.println(resultSet.getObject(2)+" "+resultSet.getObject(3));
				if(username.equals(resultSet.getObject(2).toString())) {
					if(password.equals(resultSet.getObject(3))) {
						return "ID"+ resultSet.getObject(1).toString();
					}
				}
				
			}

		} catch (SQLException e) {
			return "There is something wrong";
		} finally {
			System.out.println(closeConnection());
		}

		return "Username not correct";
	}
	
	public String isUserExist(String username) {
		openConnection();
		
		// Here we create the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			
			String getUserQuery = 
	
"SELECT username FROM "+ this.getDbTableUser();
			
			// object
			resultSet = statement.executeQuery(getUserQuery);

			// Here we get the metadata of the query results

			while (resultSet.next()) {
				if(username.equals(resultSet.getObject(1).toString())) {
					return "ID"+ resultSet.getObject(1).toString();
				}
				
			}

		} catch (SQLException e) {
			return "There is something wrong";
		} 
		return "Username not correct";
	}
	
	public ArrayList<CartUser> getUserCartData(int id) {
		openConnection();

		ArrayList<CartUser> arrayList = new ArrayList<CartUser>();
		// Here we create the statement object for executing SQL commands.
		try {

			//statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			
			String getUserQuery = "SELECT * FROM "+ this.getDbTableCart() + " WHERE id_user = ?";
			
			PreparedStatement ps = dbConnection.prepareStatement(getUserQuery);
			
			ps.setInt(1, id);
			
			// object
			resultSet = ps.executeQuery();

			// Here we get the metadata of the query results

			while (resultSet.next()) {
				CartUser cartUser = new CartUser();
				cartUser.setId_user((int) resultSet.getObject(1));
				cartUser.setSession((String) resultSet.getObject(2));
				cartUser.setFullname((String) resultSet.getObject(3));
				cartUser.setAddress((String) resultSet.getObject(4));
				cartUser.setItems((String) resultSet.getObject(5));
				cartUser.setAmounts((String) resultSet.getObject(6));
				cartUser.setPrice_unit((String) resultSet.getObject(7));
				arrayList.add(cartUser);
				System.out.println("IDDDDDDDDDDDD"+resultSet.getObject(1));
			}

		} catch (SQLException e) {
			return arrayList;
		} finally {
			System.out.println(closeConnection());
		}

		return arrayList;
	}
	
	public String createUserAccount(String username, String password) {
		StringBuilder queryResult = new StringBuilder();
		

		String insertCommand = "INSERT INTO " + this.getDbTableUser()+ "(`username`, `password`)" + " VALUES(?," +
                "AES_ENCRYPT(?,'" + key + "'))";
		
		openConnection();
		
		if(this.isUserExist(username).contains("ID")){
			return "Exist";
		}

		System.out.println(this.getDbTableUser());
		int updatedRows = 0;
		try {

			PreparedStatement ps = dbConnection.prepareStatement(insertCommand);
			// Here we set the value for the first parameter
			ps.setString(1, username);

			// Here we set the value for the second parameter
			ps.setString(2, password);
			

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			
			queryResult.append(
					"<p>Data was added to " + this.getDbTableUser() + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		System.out.println("Signup: "+queryResult);
		return "Success";
	}
	
	
	public String saveCheckOutUserData(CartUser cartUser) {
		StringBuilder queryResult = new StringBuilder();
		String insertCommand = "INSERT INTO " + this.getDbTableCart()+ 
				"(`id_user`,`session`,`fullname`,`address`,`items`,`amounts`,`price_unit`)" + " VALUES(?,?,?,?,?,?,?)";
		openConnection();
    	System.out.println("Vo database");
	    int updatedRows = 0;
		try {

			PreparedStatement ps = dbConnection.prepareStatement(insertCommand);
			// Here we set the value for the first parameter
			ps.setInt(1, cartUser.getId_user());

			// Here we set the value for the second parameter
			ps.setString(2, cartUser.getSession());
			ps.setString(3, cartUser.getFullname());
			ps.setString(4, cartUser.getAddress());
			ps.setString(5, cartUser.getItems());
			ps.setString(6, cartUser.getAmounts());
			ps.setString(7, cartUser.getPrice_unit());

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			queryResult.append(
					"<p>Data was added to " + this.getDbTableCart() + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		
		return cartUser.getId_user()+"\n "+cartUser.getItems()+"\n "+cartUser.getPrice_unit()+"\n "+cartUser.getAmounts();
	}

	public String preparedStatementInsertData(String tableRowData, InputStream fileInputStream) throws FileNotFoundException {

		StringBuilder queryResult = new StringBuilder();
		String[] columnData = tableRowData.split(";");
		

		String insertCommand = "insert into " + this.getDbTableUser() + " values (?, ?, ?, ?)";
		openConnection();

		System.out.println(this.getDbTableUser());
		int updatedRows = 0;
		try {

			PreparedStatement ps = dbConnection.prepareStatement(insertCommand);

			// Here we set the value for the first parameter
			ps.setString(1, columnData[0]);

			// Here we set the value for the second parameter
			ps.setString(2, columnData[1]);
			
			ps.setBlob(3, fileInputStream);
			
			//Here the fourth ( date ) 
			ps.setString(4, columnData[3]);

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			queryResult.append(
					"<p>Data was added to " + this.getDbTableUser() + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();

	}


	public String readImageFromDB(String dbTableName) {

		StringBuilder queryResult = new StringBuilder();

		String imageQuery = "select * from " + dbTableName;

		// Here we empty the temporary image directory
		tempImageDirAbsoluteFileObj = new File(tempImageDirAbsolute);
		if (tempImageDirAbsoluteFileObj.exists()) {
			tempImageDirAbsoluteFileObj.delete();

		}

		tempImageDirAbsoluteFileObj.mkdirs();

		queryResult.append("<h2>Results for '" + imageQuery + "':</h2>");
		queryResult.append("<table>");
		queryResult.append("<tr><th>NAME</th><th>IMAGE</th><th>IMAGE SIZE</th></tr>");
		int imageCounter = 0;
		// In the following we read data from the database.
		try {
			openConnection();
			statement = dbConnection.createStatement();

			ResultSet resultSet = statement.executeQuery(imageQuery);
			File destinationFile = null;
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			String name;

			while (resultSet.next()) {
				// Here we read the value of the first column of the table.
				name = resultSet.getString(1);
				// Here we create a File object, which refers to
				// the name read from the first column of the table
				destinationFile = new File(tempImageDirAbsolute + name);
				// Here we prepare a FileOutputStream to write to the
				// destination file.
				fileOutputStream = new FileOutputStream(destinationFile);
				// Here we initialise the inputStream by reading from
				// the second column of the table
				inputStream = resultSet.getBinaryStream(2);
				// Here we reserve memory area to read the image
				// content.
				byte[] imageBuffer = new byte[inputStream.available()];
				// Here we read the image data from the database to
				// the memory area.
				inputStream.read(imageBuffer);
				// Here write the image data from memory to the file.
				fileOutputStream.write(imageBuffer);
				// Here we close the output and input streams.
				fileOutputStream.close();

				// Here we read the size of the image from the third
				// column of the table.
				long imageSize = resultSet.getLong(3);
				queryResult.append(
						"<tr><td>" + (imageCounter + 1) + "</td><td>" + name + "</td><td>" + imageSize + "</td>");
				queryResult.append(
						"<td><img src='" + tempImageDir + name + "' alt='Error' width='150' height='150'/></td></tr>");

				imageCounter++;

			}

		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			queryResult.append(closeConnection());

			queryResult.append("<p>" + imageCounter + " images were read from '" + dbTableName + "' table.</p>");
		}
		queryResult.append("</table>");
		return queryResult.toString();

	}

	public String getDbServerURL() {
		return dbServerURL;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbTableUser() {
		return dbTableUser;
	}

	public void setDbTable(String dbTableUser) {
		this.dbTableUser = dbTableUser;
	}
	
	public String getDbTableCart() {
		return dbTableCart;
	}

	public void setDbTableCart(String dbTableCart) {
		this.dbTableCart = dbTableCart;
	}
}