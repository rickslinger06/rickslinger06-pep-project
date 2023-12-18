package Controller;

import java.util.List;

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
        app.patch("/messages/{id}", this::updateMessage);
        app.delete("/messages/{id}", this::deleteMessage);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerAccount(Context context) {

        AccountService accountService = new AccountService();
        Account newAccount = context.bodyAsClass(Account.class);
        
        try {
            if (accountService.isUserNameBlank(newAccount)) {
                context.status(400);
                return;
            }
    
            if (!accountService.isPasswordMorethan4Characters(newAccount)) {
                context.status(400);
                return;
            }
    
            if (accountService.doesUsernameExist(newAccount)) {
                context.status(400);
                return;
            }
    
            accountService.createNewAccount(newAccount);
            context.status(200).json(newAccount);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            context.status(500);
        }
    }
    

    public Account login(Context context) {
        Account account = context.bodyAsClass(Account.class);
        AccountService accountService = new AccountService();
        Account loginAccount = accountService.login(account);
    
        if (loginAccount != null) {
            context.status(200).json(loginAccount);
        } else {
            context.status(401); // Unauthorized status for failed login attempts
        }
        return loginAccount;
    }

    private void createMessage(Context context) {
        Message msg = context.bodyAsClass(Message.class);
        MessageService msgService = new MessageService();
    
        if (msgService.checkMessageTextIsBlank(msg) ||
            msgService.checkMessageIsoverLimit(msg) ||
            !msgService.isPosterAnExistingUser(msg)) {
            context.status(400);
         
        }
    
        msgService.createNewMessage(msg);
        context.status(200).json(msg); // Message successfully created

     
    }
   

    
    

    private List<Message> retrieveAllMessages(Context context){

        MessageService msgService = new MessageService();

        List<Message> listOfMessages = msgService.findAllMessages();
        context.status(200).jsonStream(listOfMessages);

      return listOfMessages;
     
    }
    private void findMessageById(Context context) {

        int id = Integer.parseInt(context.pathParam("id"));
        MessageService msgService = new MessageService();
        Message foundMessage = msgService.findByMessageId(id);
    
        if (foundMessage != null) {
            context.json(foundMessage); 
            context.status(200);
        }
    }

    private void updateMessage(Context context) {
        int id = Integer.parseInt(context.pathParam("id"));
    
        MessageService msgService = new MessageService();
        Message existingMessage = msgService.findByMessageId(id);
    
        if (existingMessage == null) {
            context.status(400);
            return;
        }
    
        String updatedText = context.formParam("message_text");
        if (updatedText == null || updatedText.isEmpty() || updatedText.length() > 255) {
            context.status(400);
            return;
        }

    
        existingMessage.setMessage_text(updatedText);
      
        Message updatedMessage = msgService.updateMessage(existingMessage);
    
        if (updatedMessage != null) {
            context.status(200).json(updatedMessage);
            return;
        }

    
    
    }
    
    

    private void deleteMessage(Context context){

      
    }


    

}