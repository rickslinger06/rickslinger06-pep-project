package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAOImpl implements MessageDAO {

    @Override
    public Message createNewMessage(Message message) {
        try (Connection cnn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO message (message_text,posted_by, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement pstm = cnn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstm.setString(1, message.getMessage_text());
            pstm.setInt(2, message.getPosted_by());
            pstm.setLong(3, message.getTime_posted_epoch());
    
            int rowsAffected = pstm.executeUpdate();
    
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1); // Assuming the ID column is the first generated key
                    message.setMessage_id(messageId); // Set the generated ID to the message object
                }
            }
        } catch (Exception e) {
            // Handle exceptions here
        }
        return message; // Return the updated message object with the generated ID
    }
    

@Override
public List<Message> findAllMessages() {
    List<Message> messages = new ArrayList<>();

    try (Connection connection = ConnectionUtil.getConnection()) {
        String sql = "SELECT * FROM message";
        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet resultSet = psmt.executeQuery();

        while (resultSet.next()) {
            // Assuming Message class constructor or setter methods to create Message objects
            Message message = new Message(
                resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch")
         
            );

            // Add the created Message object to the list
            messages.add(message);
        }
    } catch (SQLException e) {
        // Handle exceptions appropriately
        e.printStackTrace();
    }
    
    return messages;
}


@Override
public Message findById(int id) {
    try (Connection conn = ConnectionUtil.getConnection()) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            // A message with the given ID exists
            return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        } else {
            // No message found with the given ID
            return null;
        }
    } catch (Exception e) {
        // Handle exceptions appropriately
        System.out.println(e.getMessage());
    }
    return null;
}


   @Override
    public void deleteById(int id) {
    try (Connection conn = ConnectionUtil.getConnection()) {
        String sql = "DELETE FROM message WHERE message_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

       pstm.executeUpdate();
        
    } catch (Exception e) {
        // Handle exceptions appropriately
        e.printStackTrace();
    }
}


    @Override
    public Message updateMessage(Message message) {
        try (Connection cnn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement pstm = cnn.prepareStatement(sql);
            pstm.setString(1, message.getMessage_text());
            pstm.setInt(2, message.getMessage_id());
    
            int rowsAffected = pstm.executeUpdate();
    
            if (rowsAffected > 0) {
                // Update successful, return the updated message
                return findById(message.getMessage_id());
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
        }
        return null; // Return null if the update fails or on error
    }
    

    

    @Override
    public List<Message> findMessagesByUserId(int user_id) {
        List<Message> messages = new ArrayList<>();
    
        try (Connection cnn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement pstm = cnn.prepareStatement(sql);
            pstm.setInt(1, user_id);
    
            ResultSet resultSet = pstm.executeQuery();
    
            while (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            // Properly handle the exception (log it or perform necessary actions)
            e.printStackTrace(); // Example: Print the exception for demonstration purposes
            // Consider throwing a custom exception or handling it according to your application's logic
        }
    
        return messages; // Return the list of messages
    }
    
    
 

}
