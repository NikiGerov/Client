package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserData {
	private Connection connection;
	private String GET_USER = "SELECT * FROM user WHERE name = ?";
	private String ADD_FRIEND = "INSERT INTO friends_list(sender_id,receiver_id,accepted) VALUES (?,?,0)";
	private String REQ_QUERY = "SELECT user.* FROM friends_list JOIN user ON user.user_id = friends_list.sender_id WHERE friends_list.receiver_id=? AND accepted=0";
	private String GET_FRIENDS = "SELECT user.* FROM friends_list JOIN user ON user.user_id = friends_list.sender_id WHERE (friends_list.sender_id=? OR friends_list.receiver_id=?) AND accepted=1";
	private String ACC_FRIEND = "UPDATE friends_list SET accepted=1 WHERE sender_id=? AND receiver_id=?";
	private String DEC_FRIEND = "DELETE FROM friends_list WHERE sender_id=? AND receiver_id=?";
	private PreparedStatement prepStmt;
	private ResultSet resultSet;
	User user = null;
	
	public UserData() {
		connection = ConnectionConfiguration.getConnection();
	}
	
	public User getUser(String username) throws SQLException {
		prepStmt = connection.prepareStatement(GET_USER);
		prepStmt.setString(1, username);
		resultSet = prepStmt.executeQuery();
		if(resultSet.next()) {
			user = new User(resultSet.getInt("user_id"), 
					resultSet.getString("name"), resultSet.getString("password"));
		}
		return user;
	}
	
	public void sendFriendRequest(User sender, User receiver) throws SQLException {
		prepStmt = connection.prepareStatement(ADD_FRIEND);
		prepStmt.setInt(1, sender.getId());
		prepStmt.setInt(2, receiver.getId());
		prepStmt.execute();
	}
	
	public List<User> getPendingRequests(User user) throws SQLException{
		List<User> pendingRequests = new ArrayList<User>();
		prepStmt = connection.prepareStatement(REQ_QUERY);
		prepStmt.setInt(1, user.getId());
		resultSet = prepStmt.executeQuery();
		
		while(resultSet.next()) {
			pendingRequests.add(new User(resultSet.getInt("user_id"), 
					resultSet.getString("name"), resultSet.getString("password")));
		}
		
		return pendingRequests;
	}
	
	public List<User> getFriends(User user) throws SQLException{
		List<User> friendsList = new ArrayList<User>();
		prepStmt = connection.prepareStatement(GET_FRIENDS);
		prepStmt.setInt(1, user.getId());
		prepStmt.setInt(2, user.getId());
		resultSet = prepStmt.executeQuery();
		
		while(resultSet.next()) {
			friendsList.add(new User(resultSet.getInt("user_id"), 
					resultSet.getString("name"), resultSet.getString("password")));
		}
		
		return friendsList;
	}
	
	public void replyRequest(User sender, User receiver, boolean status) throws SQLException {
		if(status) {
			prepStmt = connection.prepareStatement(ACC_FRIEND);			
		} else {
			prepStmt = connection.prepareStatement(DEC_FRIEND);
		}
		prepStmt.setInt(1, sender.getId());
		prepStmt.setInt(2, receiver.getId());
		prepStmt.execute();
	}
	
}
