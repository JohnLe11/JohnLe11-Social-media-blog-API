package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

// Data Access Object (DAO) for handling Account database operations.
public class AccountDao {

    // Inserts a new account into the database and returns the Account with its auto-generated account_id.
    public Account createAccount(Account account) throws SQLException {
        // Get a database connection.
        Connection conn = ConnectionUtil.getConnection();
        // SQL insert statement for the account table.
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        // Prepare the statement and request the generated keys.
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        // Execute the update and check if at least one row was affected.
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }
        // Retrieve the auto-generated account_id.
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            account.setAccount_id(rs.getInt(1));
        }
        // Clean up and close resources.
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
    
    // Retrieves an account from the database by its username.
    public Account getAccountByUsername(String username) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        Account account = null;
        if (rs.next()){
            account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
    
    // Retrieves an account from the database by its account_id.
    public Account getAccountById(int account_id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, account_id);
        ResultSet rs = ps.executeQuery();
        Account account = null;
        if (rs.next()){
            account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
}
