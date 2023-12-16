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
       
    try(Connection cnn = ConnectionUtil.getConnection()) {
        String sql = "INSERT INTO message (posted_by, message_text) VALUES (?,?)";
        PreparedStatement pstm = cnn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstm.setInt(1, message.getPosted_by());
        pstm.setString(2, message.getMessage_text());
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void updateMesage(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMesage'");
    }

    @Override
    public List<Message> findMessagesByUserId(int user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findMessagesByUserId'");
    }
    
 

}
