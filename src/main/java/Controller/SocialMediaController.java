package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;

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

    private AccountService accountService = new AccountService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.get("example-endpoint", this::exampleHandler);

        // Registration endpoint
        app.post("/register", this::handleRegister);
        // Login endpoint
        app.post("/login", this:: handleLogin);
        // Create new message endpoint
        // Get all messages
        // Get one message by id
        // Delete a message by id
        // Update a message by id(only updates message_text)
        // Get all messages posted by a specific account

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void handleRegister(Context ctx) {
        try {
            Account account = ctx.bodyAsClass(Account.class);
            Account created = accountService.register(account);
            ctx.json(created);
        } catch(IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
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
            ctx.status(401).result(e.getMessage());
        } catch(Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }


}