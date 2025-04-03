package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDao {

    public Account createAccount(Account account) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        // Note: using table "account" (singular) per SQL file.
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            account.setAccount_id(rs.getInt(1));
        }
        rs.close();
        ps.close();
        conn.close();
        return account;
    }
    
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
