package Service;

import DAO.AccountDao;
import Model.Account;
import java.sql.SQLException;

// Service layer for account operations that contains business logic.
public class AccountService {

    private AccountDao accountDao = new AccountDao();
    
    // Registers a new account after performing validations.
    public Account register(Account account) throws SQLException {
        // Check that the username is not blank.
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        // Check that the password is at least 4 characters long.
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        // Check if an account with the same username already exists.
        Account existing = accountDao.getAccountByUsername(account.getUsername());
        if (existing != null) {
            throw new IllegalArgumentException("Account with this username already exists.");
        }
        // If validations pass, create the account.
        return accountDao.createAccount(account);
    }
    
    // Validates user credentials and returns the account if valid.
    public Account login(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty() ||
            account.getPassword() == null) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        Account existing = accountDao.getAccountByUsername(account.getUsername());
        // Verify the provided password against the stored password.
        if (existing == null || !existing.getPassword().equals(account.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return existing;
    }
}
