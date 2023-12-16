package Controller;

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
        app.post("messages", this::createMessage);

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

   private Message createMessage(Context context){

        Message msg = context.bodyAsClass(Message.class);
        MessageService msgService = new MessageService();

        if(msgService.checkMessageTextIsBlank(msg)){
            context.status(400);
        }

        if(msgService.checkMessageIsoverLimit(msg)){
            context.status(400);
        }

        if(msgService.isPosterAnExistingUser(msg)){
            context.status(400);
        }

       msgService.createNewMessage(msg);
       context.status(200).json(msg);
        return msg;


    }

}