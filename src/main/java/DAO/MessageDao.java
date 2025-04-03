package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO for handling Message database operations.
public class MessageDao {

    // Inserts a new message into the database and returns it with the generated message_id.
    public Message createMessage(Message message) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        // SQL statement to insert into the message table.
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Creating message failed, no rows affected.");
        }
        // Retrieve the auto-generated message_id.
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()){
            message.setMessage_id(rs.getInt(1));
        }
        rs.close();
        ps.close();
        conn.close();
        return message;
    }
    
    // Retrieves all messages from the database.
    public List<Message> getAllMessages() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Message> messages = new ArrayList<>();
        // Loop through the result set and build Message objects.
        while (rs.next()){
            Message message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
        rs.close();
        stmt.close();
        conn.close();
        return messages;
    }
    
    // Retrieves a single message based on message_id.
    public Message getMessageById(int message_id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, message_id);
        ResultSet rs = ps.executeQuery();
        Message message = null;
        if (rs.next()){
            message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return message;
    }
    
    // Deletes a message by its message_id and returns the deleted message.
    public Message deleteMessageById(int message_id) throws SQLException {
        // Retrieve the message first so we can return it.
        Message message = getMessageById(message_id);
        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, message_id);
        ps.executeUpdate();
        ps.close();
        conn.close();
        return message;
    }
    
    // Updates the message_text for a given message and returns the updated message.
    public Message updateMessage(int message_id, String newText) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newText);
        ps.setInt(2, message_id);
        int rowsAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        // If the update is successful, retrieve and return the updated message.
        if (rowsAffected > 0){
            return getMessageById(message_id);
        } else {
            return null;
        }
    }
    
    // Retrieves all messages posted by a specific account.
    public List<Message> getMessagesByAccountId(int account_id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, account_id);
        ResultSet rs = ps.executeQuery();
        List<Message> messages = new ArrayList<>();
        while (rs.next()){
            Message message = new Message(
                rs.getInt("message_id"), 
                rs.getInt("posted_by"),
                rs.getString("message_text"), 
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
        rs.close();
        ps.close();
        conn.close();
        return messages;
    }
}
