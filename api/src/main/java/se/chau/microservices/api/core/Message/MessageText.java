package se.chau.microservices.api.core.Message;

public class MessageText {
    private String sender;
    private String room;
    private String type;
    private String message;
    private long timestamp; // 🔥 Thêm timestamp

    public MessageText() {}

    public MessageText(String sender, String room, String type, String message, long timestamp) {
        this.sender = sender;
        this.room = room;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() { // 🔥 Thêm getter
        return timestamp;
    }

    public void setTimestamp(long timestamp) { // 🔥 Thêm setter
        this.timestamp = timestamp;
    }
}
