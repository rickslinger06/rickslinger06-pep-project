package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {

    /**
     * 
     * @param username
     * @return account of the user that matches the usernmae
     */

    @Override
    public Account findByUsername(String username) {
        Account account = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String fetchedUsername = rs.getString("username");
                String password = rs.getString("password");
                account = new Account(accountId, fetchedUsername, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

/**
 * 
 * 
 * @param username
 * @return true is the username alrady exist in the database and false otherwise;
 */
    @Override
    public boolean doesUsernameExist(String username) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Returns true if a user with the provided username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception here based on your application's error-handling strategy
        }
        return false; // Return false if an exception occurs or the query fails
    }

    /**
     * create new account
     * @param account 
     * 
     */
    @Override
    public boolean createAccount(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    account.setAccount_id(generatedId); // Set the generated ID in the account object
                    return true; // Return true to confirm successful insertion
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the account creation fails or an exception occurs
    }



    /**
     *find Acount the match the username and password of the user;
     * @param username
     * @param password
     * @return Account that that match the usernmae and password
     */
    public Account findByUsernameAndPassword(String username, String password) {
        Account account = null;
    
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int fetchId = rs.getInt("account_id");
                String fetchUsername = rs.getString("username");
                String fetchPassword = rs.getString("password");
                account = new Account(fetchId,fetchUsername, fetchPassword); // Create the account object with fetched details
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    
        return account; 
    }
    
    /**
     * 
     * @param account
     * @return the the authenticated account
     */
    public Account login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
    
        // Authenticate the user using the DAO method
        Account authenticatedAccount = findByUsernameAndPassword(username, password);
    
        return authenticatedAccount;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
    
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account";
            PreparedStatement psmt = connection.prepareStatement(sql);
            ResultSet resultSet = psmt.executeQuery();
    
            while (resultSet.next()) {
                // Assuming Account class constructor or setter methods to create Account objects
                Account account = new Account(
                    resultSet.getInt("account_id"),
                    resultSet.getString("account_name")
          
                );
    
                // Add the created Account object to the list
                accounts.add(account);
            }
        } catch (SQLException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
        
        return accounts;
    }
    
    public Account findById(int id) {
      
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setAccount_id(id);
                account.setUsername(rs.getString("username"));
          
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
        return null; // Return null if account not found or on error
    }

    
}
