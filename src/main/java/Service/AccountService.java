package Service;

import DAO.AccountDao;
import Model.Account;
import java.sql.SQLException;

public class AccountService {
    
    private AccountDao accountDao = new AccountDao();

    public Account register(Account account) throws SQLException {
        // Validation: user must not be blank
        if(account.getUsername() == null || account.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        // Validation: password must be at least 4 characters long
        if(account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        // Check that an acccount with the same username does not already exist
        Account existing = accountDao.getAccountByUsername(account.getUsername());
        if(existing != null){
            throw new IllegalArgumentException("Account with this username already exists.");
        }
        return accountDao.createAccount(account);
    }

    public Account login(Account account) throws SQLException {
        if(account.getUsername() == null || account.getUsername().trim().isEmpty() || account.getPassword() == null){
            throw new IllegalArgumentException("Invalid credentials.");
        }
        Account existing = accountDao.getAccountByUsername(account.getUsername());
        if(existing == null || !existing.getPassword().equals(account.getPassword())){
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return existing;
    }
}
