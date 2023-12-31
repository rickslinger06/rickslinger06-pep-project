package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {

    Message createNewMessage(Message msg);
    List<Message> findAllMessages();
    Message findById(int id);
    void deleteById(int id);
    Message updateMessage(Message message);
    List<Message>findMessagesByUserId(int user_id);




    
}
