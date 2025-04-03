package Model;

// This POJO is used to map the JSON request body for PATCH requests (updating a message).
public class UpdateMessageRequest {
    // Field to hold the new message text.
    private String message_text;
    
    // Default constructor (required by Jackson for JSON parsing).
    public UpdateMessageRequest() { }
    
    // Getter for message_text.
    public String getMessage_text() {
        return message_text;
    }
    
    // Setter for message_text.
    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
}
