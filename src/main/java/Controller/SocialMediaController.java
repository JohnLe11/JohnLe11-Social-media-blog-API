package Controller;

import Model.Account;
import Model.Message;
import Model.UpdateMessageRequest;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

// Controller class that defines all the REST API endpoints for the application.
public class SocialMediaController {
    
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();
    
    // Initializes and returns a Javalin app with all endpoints configured.
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Endpoint for user registration.
        app.post("/register", this::handleRegister);

        // Endpoint for user login.
        app.post("/login", this::handleLogin);

        // Endpoint for creating a new message.
        app.post("/messages", this::handleCreateMessage);

        // Endpoint for retrieving all messages.
        app.get("/messages", this::handleGetAllMessages);

        // Endpoint for retrieving a single message by its ID.
        app.get("/messages/{message_id}", this::handleGetMessageById);

        // Endpoint for deleting a message by its ID.
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);

        // Endpoint for updating a message's text (expects JSON with "message_text").
        app.patch("/messages/{message_id}", this::handleUpdateMessage);

        // Endpoint for retrieving all messages for a given account.
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccountId);
        
        return app;
    }
    
    // Handler for user registration.
    private void handleRegister(Context ctx) {
        try {
            // Parse the request body into an Account object.
            Account account = ctx.bodyAsClass(Account.class);
            // Register the account using the service.
            Account created = accountService.register(account);
            // Return the created account as JSON.
            ctx.json(created);
        } catch(IllegalArgumentException e) {
            // On validation failure, return status 400 with an empty body.
            ctx.status(400).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for user login.
    private void handleLogin(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account loggedIn = accountService.login(account);
            ctx.json(loggedIn);
        } catch(IllegalArgumentException e) {
            // On invalid credentials, return status 401 with an empty body.
            ctx.status(401).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for creating a new message.
    private void handleCreateMessage(Context ctx) {
        try {
            Message message = ctx.bodyAsClass(Message.class);
            Message created = messageService.createMessage(message);
            ctx.json(created);
        } catch(IllegalArgumentException e) {
            ctx.status(400).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for retrieving all messages.
    private void handleGetAllMessages(Context ctx) {
        try {
            ctx.json(messageService.getAllMessages());
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for retrieving a single message by its ID.
    private void handleGetMessageById(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            // If message is not found, return an empty body.
            if (message == null) {
                ctx.result("");
            } else {
                ctx.json(message);
            }
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for deleting a message by its ID.
    private void handleDeleteMessageById(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted = messageService.deleteMessageById(message_id);
            // Return the deleted message if found; otherwise, return an empty body.
            if (deleted != null) {
                ctx.json(deleted);
            } else {
                ctx.status(200).result("");
            }
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for updating a message's text.
    private void handleUpdateMessage(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            // Parse the JSON body into an UpdateMessageRequest object.
            UpdateMessageRequest updateReq = ctx.bodyAsClass(UpdateMessageRequest.class);
            String newText = updateReq.getMessage_text();
            Message updated = messageService.updateMessage(message_id, newText);
            ctx.json(updated);
        } catch(IllegalArgumentException e) {
            ctx.status(400).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    // Handler for retrieving all messages for a specific account.
    private void handleGetMessagesByAccountId(Context ctx) {
        try {
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByAccountId(account_id));
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
}
