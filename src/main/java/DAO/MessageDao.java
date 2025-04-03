package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    public Message createMessage(Message message) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        // Note: using table "message" (singular) per SQL file.
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Creating message failed, no rows affected.");
        }
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()){
            message.setMessage_id(rs.getInt(1));
        }
        rs.close();
        ps.close();
        conn.close();
        return message;
    }
    
    public List<Message> getAllMessages() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
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
        stmt.close();
        conn.close();
        return messages;
    }
    
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
    
    public Message deleteMessageById(int message_id) throws SQLException {
        // Retrieve the message before deletion so it can be returned
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
    
    public Message updateMessage(int message_id, String newText) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newText);
        ps.setInt(2, message_id);
        int rowsAffected = ps.executeUpdate();
        ps.close();
        conn.close();
        if (rowsAffected > 0){
            return getMessageById(message_id);
        } else {
            return null;
        }
    }
    
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
