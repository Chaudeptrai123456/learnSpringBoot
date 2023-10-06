package com.example.Aiking.DTO;

import jakarta.persistence.Column;

import java.util.Date;

public class PaymentPOJO {
    private String cardId;
    private String fullName;
    private String nameCard;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }


}
