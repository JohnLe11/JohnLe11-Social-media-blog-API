package Service;

import DAO.MessageDao;
import DAO.AccountDao;
import Model.Message;
import Model.Account;
import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private MessageDao messageDao = new MessageDao();
    private AccountDao accountDao = new AccountDao();
    
    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        // Validate that the posting account exists
        Account account = accountDao.getAccountById(message.getPosted_by());
        if (account == null) {
            throw new IllegalArgumentException("Posted_by account does not exist.");
        }
        return messageDao.createMessage(message);
    }
    
    public List<Message> getAllMessages() throws SQLException {
        return messageDao.getAllMessages();
    }
    
    public Message getMessageById(int message_id) throws SQLException {
        return messageDao.getMessageById(message_id);
    }
    
    public Message deleteMessageById(int message_id) throws SQLException {
        return messageDao.deleteMessageById(message_id);
    }
    
    public Message updateMessage(int message_id, String newText) throws SQLException {
        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if (newText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        // Ensure the message exists before updating
        Message existing = messageDao.getMessageById(message_id);
        if (existing == null) {
            throw new IllegalArgumentException("Message does not exist.");
        }
        return messageDao.updateMessage(message_id, newText);
    }
    
    public List<Message> getMessagesByAccountId(int account_id) throws SQLException {
        return messageDao.getMessagesByAccountId(account_id);
    }
}
