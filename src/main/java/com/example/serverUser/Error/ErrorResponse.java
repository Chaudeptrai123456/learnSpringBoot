package com.example.serverUser.Error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ErrorResponse extends RuntimeException{
    private String message;

    public ErrorResponse( String message) {
        super(message);
        this.message = message;
    }
}
