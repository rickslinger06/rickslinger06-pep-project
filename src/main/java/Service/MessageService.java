package Service;

import java.util.List;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

public class MessageService {

    MessageDAOImpl msgDAO;
    AccountDAOImpl acctDAO;


     /**
      * Dependency injection
     * @param msgDAO
     * @param acctDAO
     * 
    */
    public MessageService(MessageDAOImpl msgDao,   AccountDAOImpl acctDAO){
        this.msgDAO =msgDao;
        this.acctDAO = acctDAO;
    }

     /**
     * @param msg RequestBody
     * @return Message object if successfully created
    */
    public Message createNewMessage(Message msg) {
        return msgDAO.createNewMessage(msg);
    }


     /**
     * @param msg RequestBody
     * @return true if the message_text is Blank
    */
    public boolean checkMessageTextIsBlank(Message msg){
        return msg.getMessage_text().isBlank();
        
    }


     /**
     * @param msg Requestbody
     * @return true if the message length is over 255 characters
    */
    public boolean checkMessageIsoverLimit(Message msg){
        return msg.getMessage_text().length() > 255 ? true:false;
    }

     /**
     * @param msg Requestbody
     * @return true if user is on database already and false otherwise
    */
    public boolean isPosterAnExistingUser(Message msg){
        int posterId = msg.getPosted_by();
        Account account = acctDAO.findById(posterId);
    
        return account != null;
    }
    

     /**
     * @param id 
     * @return List all Messages
    */
    public List<Message> findAllMessages(){

        List<Message> listOfMessages = msgDAO.findAllMessages();
        return listOfMessages;
    }


     /**
     * @param id 
     * @return Message by message_id
    */
    public Message findByMessageId(int id){
        return msgDAO.findById(id);
    }


     /**
     * @param message
     * @return updated object message
    */
    public Message updateMessage(Message message){
        return msgDAO.updateMessage(message);
    }

     /**
     * @param id 
     * Delete the message by message_id
    */
    public void deleteMessage(int id){

        msgDAO.deleteById(id);
    }

    /**
     * @param id 
     * @return List of Messages buy poster_by(user)
    */
    public List<Message> findAllMessageByUserId(int id){

        List<Message> list = msgDAO.findMessagesByUserId(id);
        return list;

    }
    
}
