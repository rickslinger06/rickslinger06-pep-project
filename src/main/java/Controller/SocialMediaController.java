package Controller;

import java.util.List;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    MessageDAOImpl msgDAo = new MessageDAOImpl();
    AccountDAOImpl acctDAO = new AccountDAOImpl();
    MessageService msgService = new MessageService(msgDAo, acctDAO);

 
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::retrieveAllMessages);
        app.get("/messages/{id}", this::findMessageById); 
        app.get("/accounts/{account_id}/messages", this::findMessagesByUserId); 
        app.patch("/messages/{id}", this::updateMessage);
        app.delete("/messages/{id}", this::deleteMessage);

        return app;
    }

    /**
     * register the account if all validation are met
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerAccount(Context context) {

        AccountService accountService = new AccountService();
        Account newAccount = context.bodyAsClass(Account.class);
        
        try {
        
           //check is the username is blank
            if (accountService.isUserNameBlank(newAccount)) {
                context.status(400);
                return;
            }
    
            //check if the password is more than 4 characters long
            if (!accountService.isPasswordMorethan4Characters(newAccount)) {
                context.status(400);
                return;
            }
    
            //check if the username already exist in the database
            if (accountService.doesUsernameExist(newAccount)) {
                context.status(400);
                return;
            }
    
            //create the account if all validations are satisfied
            accountService.createNewAccount(newAccount);
            context.status(200).json(newAccount);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            context.status(500);
        }
    }
    

     /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return loginAccount object
     */
    public Account login(Context context) {
        Account account = context.bodyAsClass(Account.class);
        AccountService accountService = new AccountService();
        Account loginAccount = accountService.login(account);
    
        //return satus 200 if successfully created new account
        if (loginAccount != null) {
            context.status(200).json(loginAccount);
        } else {
            // Unauthorized status for failed login attempts
            context.status(401); 
        }
        return loginAccount;
    }

     /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * create message method
     */
    private void createMessage(Context context) {
        Message msg = context.bodyAsClass(Message.class);
        
        try {
            //check if the poster of the message have an existing account
            if (!msgService.isPosterAnExistingUser(msg)) {
                context.status(400);
          
            }
        //check if the message_text is not over 255 characters
            if (msgService.checkMessageIsoverLimit(msg)) {
                context.status(400);
                return;
            }
    
            //check if the message is blank
            if (msgService.checkMessageTextIsBlank(msg)) {
                context.status(400);
                return;
            }
    
            // Create the message if all conditions are met
            Message createdMessage = msgService.createNewMessage(msg);
            if (createdMessage != null && createdMessage.getMessage_id() > 0) {
                context.status(200).json(createdMessage);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.status(500).result("Internal Server Error");
        }
    }

 /**
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @return List of Message retrieve from database
     */
    private List<Message> retrieveAllMessages(Context context){

        List<Message> listOfMessages = msgService.findAllMessages();
        context.status(200).jsonStream(listOfMessages);

      return listOfMessages;
     
    }
 /**
     * 
     * find the message by message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     */
    private void findMessageById(Context context) {

        int id = Integer.parseInt(context.pathParam("id"));
        Message foundMessage = msgService.findByMessageId(id);
    
        if (foundMessage != null) {
            context.json(foundMessage); 
            context.status(200);
        }
    }

     /**
     * 
     * Update the message if the message already exist 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     */
    private void updateMessage(Context context) {
        int id = Integer.parseInt(context.pathParam("id"));

        Message message = context.bodyAsClass(Message.class);
    
        //find the message to be updated by message_id
        Message existingMessage = msgService.findByMessageId(id);

        if(existingMessage == null){
            context.status(400);
            return;
        }

        //set the new message
        existingMessage.setMessage_text(message.getMessage_text());

        if(existingMessage.getMessage_text().length() > 255 ||
            existingMessage.getMessage_text().isBlank() ){
            context.status(400);
            return;
        }

        //update the message if all validation are met
        msgService.updateMessage(existingMessage);
        context.status(200).json(existingMessage);

        
    }
    
    
    
 /**
     * delete message by message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     */
    private void deleteMessage(Context context){
        int id = Integer.parseInt(context.pathParam("id"));
    
        Message foundMessage = msgService.findByMessageId(id);
        
        if (foundMessage != null) {
            msgService.deleteMessage(id);
            context.status(200).json(foundMessage);
        } else {
            context.status(200).result("");
        }
    }
    
     /**
     * find message by poster_id (user_id)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     */
    public void findMessagesByUserId(Context context){
        int id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> listOfMessages = msgDAo.findMessagesByUserId(id);

            context.status(200).json(listOfMessages);

    }


    

}