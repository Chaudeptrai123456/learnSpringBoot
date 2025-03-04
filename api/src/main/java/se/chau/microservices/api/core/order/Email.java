package se.chau.microservices.api.core.order;

public class Email {
    private int userId;
    private String email;
    private String username;

    private Double totalCost;

    private String token;

    private int orderId;

    private String link;

    public Email() {
        this.userId = 0;
        this.email = "";
        this.username = "";
        this.totalCost = 0.0;
        this.orderId = 0;
    }


    public Email(int userId, String email, String username, Double totalCost, String token, int orderId, String link) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.totalCost = totalCost;
        this.token = token;
        this.orderId = orderId;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
