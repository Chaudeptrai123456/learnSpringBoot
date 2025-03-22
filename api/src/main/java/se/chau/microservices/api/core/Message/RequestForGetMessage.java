package se.chau.microservices.api.core.Message;

public class RequestForGetMessage {

    private String roomId;

    public RequestForGetMessage(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
