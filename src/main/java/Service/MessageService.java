package Service;

import DAO.MessageDao;
import DAO.AccountDao;
import Model.Message;
import Model.Account;
import java.sql.SQLException;
import java.util.List;

// Service layer for message operations that includes validations and business rules.
public class MessageService {

    private MessageDao messageDao = new MessageDao();
    private AccountDao accountDao = new AccountDao();
    
    // Creates a new message after validating content and ensuring the posting account exists.
    public Message createMessage(Message message) throws SQLException {
        // Ensure the message text is not blank.
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        // Ensure the message text does not exceed 255 characters.
        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        // Verify that the account posting the message exists.
        Account account = accountDao.getAccountById(message.getPosted_by());
        if (account == null) {
            throw new IllegalArgumentException("Posted_by account does not exist.");
        }
        // Create the message in the database.
        return messageDao.createMessage(message);
    }
    
    // Retrieves all messages.
    public List<Message> getAllMessages() throws SQLException {
        return messageDao.getAllMessages();
    }
    
    // Retrieves a single message by its ID.
    public Message getMessageById(int message_id) throws SQLException {
        return messageDao.getMessageById(message_id);
    }
    
    // Deletes a message and returns the deleted message.
    public Message deleteMessageById(int message_id) throws SQLException {
        return messageDao.deleteMessageById(message_id);
    }
    
    // Updates a message's text after validating the new text.
    public Message updateMessage(int message_id, String newText) throws SQLException {
        // Validate the new text is not blank.
        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        // Validate the new text length.
        if (newText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }
        // Ensure the message exists before updating.
        Message existing = messageDao.getMessageById(message_id);
        if (existing == null) {
            throw new IllegalArgumentException("Message does not exist.");
        }
        return messageDao.updateMessage(message_id, newText);
    }
    
    // Retrieves all messages for a given account.
    public List<Message> getMessagesByAccountId(int account_id) throws SQLException {
        return messageDao.getMessagesByAccountId(account_id);
    }
}
