package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Order;

import org.json.JSONObject;

public class DBConnection {

	private Connection conn;	

	public DBConnection() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(DBUtility.URL);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error from /src/db/DBConnection -> " + e.getMessage());
		}
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getParticularStationByCoordinate(double lat, double lon) {
		if (conn == null) {
			return null;
		}
		String s = "";
		try {
			String sql = "SELECT station_id FROM stations WHERE station_lat = ? AND station_lon = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				s = rs.getString("station_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String getParticularStationByPlace(String street_num, String street_name, String city) {
		if (conn == null) {
			return null;
		}
		String s = "";
		try {
//			System.out.println("num is "+ street_num+", name is "+street_name+", city is " + city);
			String sql = "SELECT station_id FROM stations WHERE street_number = ? AND street_name = ? AND city = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, street_num);
			stmt.setString(2, street_name);
			stmt.setString(3, city);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				s = rs.getString("station_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String getAvailableCourier(String station, String type) {
		if (conn == null) {
			return null;
		}
		
		try {
			String sql = "SELECT courier_id, time FROM couriers WHERE station_id = ? AND type = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, station);
			stmt.setString(2, type);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String courierId = rs.getString("courier_id");
				Timestamp time = rs.getTimestamp("time");				
				Timestamp curTime = new Timestamp((new Date()).getTime());
				if (curTime.after(time)) {
					return courierId;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<String> getStation() {
		if (conn == null) {
			return null;
		}
		List<String> stationList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT station_id FROM stations";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				stationList.add(rs.getString("station_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stationList;
	}
	
	
	public void setReservation(Order ord) {
		
		if (conn == null) {
			System.err.println("DB connection failed from src/db/DBConnection -> setReservation");
			return;
		}
		
		try {
			
			 String sql = "INSERT IGNORE INTO orders VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	   		 PreparedStatement ps = conn.prepareStatement(sql);
	   		 ps.setString(1,  ord.getOrderId());
	   		 ps.setString(2, ord.getUserId());
	   		 ps.setString(3, ord.getCourierId());
	   		 ps.setString(4, ord.getItemId());
	   		 ps.setString(5, ord.getType());
	   		 ps.setString(6,  ord.getStartStreeNumber());
	   		 ps.setString(7,  ord.getStartStreeName());
	   		 ps.setString(8,  ord.getStartCity());
	   		 ps.setString(9,  ord.getEndStreeNumber());
	   		 ps.setString(10,  ord.getEndStreeName());
	   		 ps.setString(11, ord.getEndCity());
	   		 ps.setString(12, ord.getStatus());
	   		 ps.setDouble(13, ord.getRouteDuration());
	   		 ps.setDouble(14, ord.getRouteDistance());
	   		 ps.setDouble(15, ord.getRoutePrice());
	   		 ps.setString(16, ord.getRoutePath());
	   		 ps.setBoolean(17, ord.isComplete());
	   		 ps.execute();
			
		}  catch(Exception e) {
			e.printStackTrace();
			System.out.println("error from src/db/DBConnection -> setReservation: " + e.getMessage());
		}
	}
	
	public String findOrderNumber(String UserId) {
		
		if (conn == null) {
			System.err.println("DB connection failed from src/db/DBConnection -> findOrderNumber");
			return null;
		}
		try {
			String sql = "SELECT order_id FROM orders WHERE user_id = ? AND COMPLETE = FALSE";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,UserId);
			ResultSet rs = stmt.executeQuery();
			String orderId = null;
			while (rs.next()) {
				orderId = rs.getString("order_id");
			}
			
			sql = "UPDATE orders SET complete = TRUE WHERE user_id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, UserId);
			
			return orderId;
		}  catch (Exception e) {
			e.printStackTrace();
			System.out.println("error from src/db/DBConnection -> findOrderNumber: " + e.getMessage());
		}
		return null;
		
	}
	
	public void setPaymentInfo(JSONObject input) {
	
		if (conn == null) {
			System.err.println("DB connection failed from src/db/DBConnection -> setPaymentInfo");
			return;
		}

		try {
			
			 String sql = "SELECT address_line1, zipcode FROM payment WHERE user_id = ? AND card_number = ? ";
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 stmt.setString(1,input.getString("user_id"));
			 stmt.setString(2,input.getString("card_number"));
			 ResultSet rs = stmt.executeQuery();
			 
			 String cardAddress = null;
			 int zipcode = -1;
			 
			 while (rs.next()) {
				 cardAddress = rs.getString("address_line1");
				 zipcode = rs.getInt("zipcode");
			 }
			 
			 if (cardAddress == null || cardAddress.length() == 0 && zipcode == -1) {
				 sql = "INSERT IGNORE INTO payment VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		   		 PreparedStatement ps = conn.prepareStatement(sql);
		   		 ps.setString(1, input.getString("user_id"));
		   		 ps.setString(2, input.getString("last_name"));
		   		 ps.setString(3, input.getString("first_name"));
		   		 ps.setString(4, input.getString("card_number"));
		   		 ps.setString(5, input.getString("address_line1"));
		   		 ps.setString(6, input.getString("address_line2"));
		   		 ps.setString(7, input.getString("city"));
		   		 ps.setInt(8, input.getInt("zipcode"));
		   		 ps.setString(9, input.getString("state"));
		   		 ps.setInt(10, input.getInt("month"));
		   		 ps.setInt(11, input.getInt("year"));
		   		 ps.setInt(12, input.getInt("cvv"));
		   		 ps.execute();
		   		 
		   		 return;
			 }  else if (!cardAddress.equals(input.getString("address_line1")) || zipcode != input.getInt("zipcode")) {
				 
				 sql = "UPDATE payment SET address_line1 = ?, zipcode = ?, address_line2 = ? , city = ?, state = ? WHERE user_id = ? AND card_number = ?";
				 PreparedStatement ps = conn.prepareStatement(sql);
				 ps.setString(1, input.getString("address_line1"));
				 ps.setInt(2, input.getInt("zipcode"));
				 ps.setString(3, input.getString("address_line2"));
				 ps.setString(4, input.getString("city"));
				 ps.setString(5, input.getString("state"));
				 ps.setString(6, input.getString("user_id"));
				 ps.setString(7, input.getString("card_number"));
				 int rowsUpdated = ps.executeUpdate();
				 if (rowsUpdated > 0) {
				     System.out.println("An existing user was updated successfully!");
				 }
				 return;
			 }  else {
				 return;
			 }
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error from src/db/DBConnection -> setPaymentInfo: " + e.getMessage());
		}
	}
	
	public boolean getPaymentInfo(String userId) {
		
		if (conn == null) {
			System.err.println("DB connection failed from src/db/DBConnection -> getPaymentInfo");
		}
		String str = null;
		try {
			String sql = "SELECT * FROM payment WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				str = rs.getString("card_number");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error from src/db/DBConnection -> getPaymentInfo " + e.getMessage());
		}
		
		return str != null;
	}

	public String getStatus(String orderID) {
		if (conn == null) {
			return null;
		}
		String status = "";
		try {
			String sql = "SELECT status FROM orders WHERE order_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				status = rs.getString("status");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public JSONArray getStationAddress() {
		if (conn == null) {
			return null;
		}
		JSONArray array =  new JSONArray();
		try {
			String sql = "SELECT street_number, street_name, city FROM stations";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("street_number", rs.getString("street_number"));
				obj.put("street_name", rs.getString("street_name"));
				obj.put("city", rs.getString("city"));
				System.out.println(obj);
				array.put(obj);
			}
			
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	public boolean signup(String userId, String password, String firstname, String lastname) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "INSERT INTO users (user_id, password, first_name, last_name) VALUES (?,?,?,?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);
			statement.setString(3, firstname);
			statement.setString(4, lastname);
			int rs = statement.executeUpdate();
			if (rs == 1) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return false; // Dummy
	}

	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		if (conn == null) {
			return "";
		}
		String name = "";

		Set<String> categories = new HashSet<>();
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return name;
	}

	public boolean verifyLogin(String userId, String password) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return false;

	}
	
	
}