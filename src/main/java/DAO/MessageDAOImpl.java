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
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement pstm = cnn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, message.getPosted_by());
            pstm.setString(2, message.getMessage_text());
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
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
                // Update successful, fetch the updated message from the database
                return findById(message.getMessage_id());
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
        }
        return null; // Return null if the update fails or on error
    }

    

    @Override
    public List<Message> findMessagesByUserId(int user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findMessagesByUserId'");
    }
    
 

}
