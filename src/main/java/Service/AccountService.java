package Service;

import DAO.AccountDAOImpl;
import Model.Account;
import Model.Message;

public class AccountService {

    AccountDAOImpl acctDAO = new AccountDAOImpl();
    

       /**
      * 
     * @param acct
     * @return true if username is blank, false otherwise
     */
    public boolean isUserNameBlank(Account acct){
        return acct.getUsername().isBlank();
    }

        /**
      * 
     * @param acct
     * @return true if password is more than 4 characters, false otherwise
     */
    public boolean isPasswordMorethan4Characters(Account acct){
        return acct.getPassword().length() >= 4;
    }

        /**
      * 
     * @param acct
     * @return If the account exists, return true
     */
    public boolean doesUsernameExist(Account acct){
        Account existedAccount = acctDAO.findByUsername(acct.getUsername());
        return existedAccount != null; 
    }

         /**
      * 
     * @param acct
     * @return creates new account
     */
    public void createNewAccount(Account account){
        acctDAO.createAccount(account);
    }

         /**
      * 
     * @param acct
     * @return authenticatedAccount object is log in is successful
     */
    public Account login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
    
        // Authenticate the user using the DAO method
        Account authenticatedAccount = acctDAO.findByUsernameAndPassword(username, password);
    
        return authenticatedAccount;
    }

  
}
