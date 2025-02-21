package se.chau.microservices.api.core.User;

public class UserReqInfo {
    private int userId;

    public UserReqInfo(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
