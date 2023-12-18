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
     /**
      * 
     * @param message
     * @return Return the updated message object with the auto generated ID
     */
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
                    // the ID column is the first generated key
                    int messageId = generatedKeys.getInt(1); 
                    // Set the generated ID to the message object
                    message.setMessage_id(messageId); 
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return message;
    }
    

      /**
      * 
     * 
     * @return List of all messages
     */
@Override
public List<Message> findAllMessages() {
    List<Message> messages = new ArrayList<>();

    try (Connection connection = ConnectionUtil.getConnection()) {
        String sql = "SELECT * FROM message";
        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet resultSet = psmt.executeQuery();

        while (resultSet.next()) {
            // Message class constructor or setter methods to create Message objects
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
        e.getMessage();
    }
    
    return messages;
}

  /**
      * 
     * @param id 
     * @return Return the message object by message_id
     */
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
        e.getMessage();
    }
    return null;
}


  /**
      * 
     * @param id
     * delete the message by message_id
     */
   @Override
    public void deleteById(int id) {
    try (Connection conn = ConnectionUtil.getConnection()) {
        String sql = "DELETE FROM message WHERE message_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        //execute the the statement
       pstm.executeUpdate();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}


  /**
      * 
     * @param message
     * @return Return the updated message object
     */
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
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        // Return null if the update fails or on error
        return null; 
    }
    
     /**
      * 
     * @param user_id
     * @return Return List of messages by user_id
     */
    @Override
    public List<Message> findMessagesByUserId(int user_id) {
        List<Message> messages = new ArrayList<>();
    
        try (Connection cnn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement pstm = cnn.prepareStatement(sql);
            pstm.setInt(1, user_id);
    
            ResultSet resultSet = pstm.executeQuery();
            
            //looping all the messages posted by user_id and created new message
            while (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );

                //adding to the list
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 

        }
    
        // Return the list of messages
        return messages; 
    }
    
    
 

}
