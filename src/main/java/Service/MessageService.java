package Service;

import java.util.List;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

public class MessageService {

    MessageDAOImpl msgDAO;


    public MessageService(MessageDAOImpl msgDao){
        this.msgDAO =msgDao;
    }

  


    public Message createNewMessage(Message msg) {
        MessageDAOImpl msgDAO = new MessageDAOImpl();
        return msgDAO.createNewMessage(msg);
    }

    public boolean checkMessageTextIsBlank(Message msg){
        

        return msg.getMessage_text().isBlank();
        
    }

    public boolean checkMessageIsoverLimit(Message msg){
        return msg.getMessage_text().length() > 255 ? true:false;
    }

    public boolean isPosterAnExistingUser(Message msg){
        int posterId = msg.getPosted_by();
        AccountDAOImpl acctDAO = new AccountDAOImpl();
        Account account = acctDAO.findById(posterId);
    
        return account != null;
    }
    

    public List<Message> findAllMessages(){

        List<Message> listOfMessages = msgDAO.findAllMessages();

        return listOfMessages;
    }

    public Message findByMessageId(int id){

        return msgDAO.findById(id);
    }

    public Message updateMessage(Message message){

        return msgDAO.updateMessage(message);
    }

    public void deleteMessage(int id){

        msgDAO.deleteById(id);
    }

    public List<Message> findAllMessageByUserId(int id){

        List<Message> list = msgDAO.findMessagesByUserId(id);
        return list;

    }
    
}
