package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Run this application to reset database schema
 */
public class DBTableCreation {

	public static void main(String[] args) {
		
		try {
			// Connect to MySQL.
			System.out.println("Connecting to " + DBUtility.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(DBUtility.URL);
			
			if (conn == null) {
				return;
			}
			
			// Drop tables in case they exist
			Statement statement = conn.createStatement();
			
			String sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS address";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users_end";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS stations";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS couriers";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS orders";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS items";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS payment";
			statement.executeUpdate(sql);

			// Create new tables
			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL," 
					+ "last_name VARCHAR(255),"
					+ "first_name VARCHAR(255),"
					+ "address_id VARCHAR(255),"
					+ "zipcode VARCHAR(255),"
					// FIXME
					+ "PRIMARY KEY (user_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE address ("
					+ "address_id VARCHAR(255) NOT NULL,"
					+ "street_num VARCHAR(255)," 
					+ "street_name VARCHAR(255),"
					+ "city VARCHAR(255),"
					+ "state VARCHAR(255),"
					+ "PRIMARY KEY (address_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			
			sql = "INSERT INTO address VALUES('11', '32', 'angle st', 'LA', 'CA')";
			statement.executeUpdate(sql);
//
//			sql = "CREATE TABLE users_end ("
//					+ "user_id VARCHAR(255) NOT NULL,"
//					+ "last_name VARCHAR(255),"
//					+ "first_name VARCHAR(255),"
//					+ "address VARCHAR(255),"
//					+ "zipcode VARCHAR(255),"
//					// FIXME
////					+ "PRIMARY KEY (user_id)"
//					+ ")";
//			statement.executeUpdate(sql);
//			
			sql = "CREATE TABLE stations ("
					+ "station_id VARCHAR(255) NOT NULL,"
//					+ "courier_num_air INT,"
//					+ "courier_num_bot INT,"
//					+ "coord VARCHAR(255) NOT NULL,"
					+ "station_lat	DOUBLE NOT NULL,"
					+ "station_lon	DOUBLE NOT NULL,"
					+ "street_number VARCHAR(255),"
					+ "street_name VARCHAR(255),"
					+ "city VARCHAR(255),"
					// FIXME
					+ "PRIMARY KEY (station_id)"
					+ ")";
			statement.executeUpdate(sql);
			
//			sql = "INSERT 			sql = "INSERT INTO stations VALUES('11', 32.834517, -117.160147, 7373, 'Convoy Ct','San Diego')";
//			statement.executeUpdate(sql);
//			statement.executeUpdate(sql);
			
			sql = "INSERT INTO stations VALUES('22', 32.8777831, -117.1859253, 5716,'Miramar Rd','San Diego')";

			statement.executeUpdate(sql);
			sql = "INSERT INTO stations VALUES('33', 32.8205483, -117.2250157, 4605,'Morena Blvd','San Diego')";
			statement.executeUpdate(sql);
			
//
			sql = "CREATE TABLE couriers ("
					+ "courier_id VARCHAR(255) NOT NULL,"
					+ "type VARCHAR(255) NOT NULL,"
//					+ "price FLOAT,"
					+ "station_id VARCHAR(255) NOT NULL,"
//					+ "order VARCHAR(255) NOT NULL,"
					+ "time TIMESTAMP NOT NULL,"
					// FIXME
					+ "PRIMARY KEY (courier_id)"
//					+ "FOREIGN KEY (station_id) REFERENCES items(station_id)"
					+ ")";
			
			statement.executeUpdate(sql);
			sql = "INSERT INTO couriers VALUES('111', 'Robot', '11', CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
			sql = "INSERT INTO couriers VALUES('222', 'Air', '11', CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
			sql = "INSERT INTO couriers VALUES('211', 'Robot', '22', CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
			
			// insert a future timestamp
			Timestamp s = new Timestamp((new Date()).getTime() + 300000);
			sql = "INSERT INTO couriers VALUES('321', 'Robot', '33', ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setTimestamp(1, s);
			stmt.executeUpdate();
			
			sql = "INSERT INTO couriers VALUES('311', 'Air', '33', ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setTimestamp(1, s);
			stmt.executeUpdate();
					
//			sql = "CREATE TABLE items ("
//					+ "item_id VARCHAR(255) NOT NULL,"
//					+ "weight FLOAT,"
//					// FIXME
////					+ "PRIMARY KEY (item_id)"
//					+ ")";
//			statement.executeUpdate(sql);

			// TODO
//			sql = "INSERT INTO orders VALUES('123k11','Delivered')";
//			statement.executeUpdate(sql);
//			sql = "INSERT INTO orders VALUES('123k1','In Transit')";
//			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE	orders (" 
					 +"order_id VARCHAR(255) NOT NULL,"
					 +"user_id VARCHAR(255) NOT NULL,"
					 +"courier_id VARCHAR(255) NOT NULL,"
					 +"item_id VARCHAR(255) NOT NULL,"
					 +"type VARCHAR(255) NOT NULL,"
					 +"start_address_id VARCHAR(255) NOT NULL,"
					 +"end_address_id VARCHAR(255) NOT NULL,"
					 +"status VARCHAR(255) NOT NULL,"
					 +"route_duration DOUBLE NOT NULL,"
					 +"route_distance DOUBLE NOT NULL,"
					 +"route_price DOUBLE NOT NULL,"
					 +"route_path VARCHAR(255) NOT NULL,"
					 +"complete BOOLEAN"
					 +")";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO orders VALUES('sfogbwklskansbbvncs012e','123','xyz','111','D',"
					+ " 'La Jolla', 'San Diego', 'TRANSIT',"
					+ "996.0, 11.25, 29.83, 'k}qcFjushVf@QFABABAF?D?D@RBB@D?J?N?B?n@JLJJ@LDTDNDNDFBFBDBDBFBFBHB',"
					+ "TRUE)";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE	payment (" 
					 +"user_id VARCHAR(255) NOT NULL,"
					 +"last_name VARCHAR(255) NOT NULL,"
					 +"first_name VARCHAR(255) NOT NULL,"
					 +"card_number VARCHAR(255) NOT NULL,"
					 +"address_line1 VARCHAR(255) NOT NULL,"
					 +"address_line2 VARCHAR(255),"
					 +"city VARCHAR(255) NOT NULL,"
					 +"zipcode INT NOT NULL,"
					 +"state VARCHAR(255) NOT NULL,"
					 +"month INT NOT NULL,"
					 +"year INT NOT NULL,"
					 +"cvv INT NOT NULL"
					 +")";
			
			
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO payment VALUES('2233', 'wu', 'sicheng','xxxx-xxxx-xxxx-xxxx',"
					+ " '3001 S. Michigan Ave', '', 'Chicago', 60616, 'IL', 04, 2018, 907)";
			statement.executeUpdate(sql);

			conn.close();
			System.out.println("Import done successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
