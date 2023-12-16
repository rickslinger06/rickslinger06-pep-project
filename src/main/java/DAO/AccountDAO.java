package DAO;

import java.util.List;

import Model.Account;

public interface AccountDAO {

    Account findByUsername(String username);
    boolean doesUsernameExist(String username);
    boolean createAccount(Account account);
    List<Account> findAllAccounts();

    
}
