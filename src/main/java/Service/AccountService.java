package Service;

import DAO.AccountDAOImpl;
import Model.Account;
import Model.Message;

public class AccountService {

    AccountDAOImpl acctDAO = new AccountDAOImpl();
    
    public boolean isUserNameBlank(Account acct){
        return acct.getUsername().isBlank();
    }

    public boolean isPasswordMorethan4Characters(Account acct){
        return acct.getPassword().length() >= 4;
    }

    public boolean doesUsernameExist(Account acct){
        Account existedAccount = acctDAO.findByUsername(acct.getUsername());
        return existedAccount != null; // If the account exists, return true
    }

    public void createNewAccount(Account account){
        acctDAO.createAccount(account);
    }

    public Account login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
    
        // Authenticate the user using the DAO method
        Account authenticatedAccount = acctDAO.findByUsernameAndPassword(username, password);
    
        return authenticatedAccount;
    }

  
}
