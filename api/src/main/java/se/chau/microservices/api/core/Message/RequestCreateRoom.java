package se.chau.microservices.api.core.Message;

public class RequestCreateRoom {
    private String roomId;
    public RequestCreateRoom() {}

    public RequestCreateRoom(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
