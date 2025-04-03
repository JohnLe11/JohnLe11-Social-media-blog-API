package Controller;

import Model.Account;
import Model.Message;
import Model.UpdateMessageRequest;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // User Registration
        app.post("/register", this::handleRegister);

        // Login
        app.post("/login", this::handleLogin);

        // Create a new message
        app.post("/messages", this::handleCreateMessage);

        // Get all messages
        app.get("/messages", this::handleGetAllMessages);

        // Get one message by message_id
        app.get("/messages/{message_id}", this::handleGetMessageById);

        // Delete a message by message_id
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);

        // Update a message by message_id (expects JSON with "message_text")
        app.patch("/messages/{message_id}", this::handleUpdateMessage);

        // Get all messages for a given account_id
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccountId);
        
        return app;
    }
    
    private void handleRegister(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account created = accountService.register(account);
            ctx.json(created);
        } catch(IllegalArgumentException e) {
            ctx.status(400).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    private void handleLogin(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account loggedIn = accountService.login(account);
            ctx.json(loggedIn);
        } catch(IllegalArgumentException e) {
            ctx.status(401).result("");
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
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
    
    private void handleGetAllMessages(Context ctx) {
        try {
            ctx.json(messageService.getAllMessages());
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    private void handleGetMessageById(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if (message == null) {
                ctx.result("");
            } else {
                ctx.json(message);
            }
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    private void handleDeleteMessageById(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted = messageService.deleteMessageById(message_id);
            if (deleted != null) {
                ctx.json(deleted);
            } else {
                ctx.status(200).result("");
            }
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
    
    private void handleUpdateMessage(Context ctx) {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            // Parse JSON body to extract "message_text"
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
    
    private void handleGetMessagesByAccountId(Context ctx) {
        try {
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByAccountId(account_id));
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }
}
