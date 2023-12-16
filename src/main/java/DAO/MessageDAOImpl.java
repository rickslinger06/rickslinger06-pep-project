package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    
 

}
