package Service;

import java.util.List;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

public class MessageService {

    MessageDAOImpl msgDAO = new MessageDAOImpl();


    public void createNewMessage(Message msg) {
        msgDAO.createNewMessage(msg);
    }

    public boolean checkMessageTextIsBlank(Message msg){

        return msg.getMessage_text().isBlank();
    }

    public boolean checkMessageIsoverLimit(Message msg){
        return msg.getMessage_text().length() > 255 ? true:false;
    }

    public boolean isPosterAnExistingUser(Message msg){
        AccountDAOImpl acctDAO = new AccountDAOImpl();

        List<Account> listOfAccounts = acctDAO.findAllAccounts();

        for(Account i : listOfAccounts){
            if(msg.getMessage_id() == i.getAccount_id()){
                return false;
            }
        }

    return true;

    }
    
}
