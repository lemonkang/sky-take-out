package org.example.exception;

import lombok.Data;

@Data
public class DeficiencyException extends Exception {
    private Object data;
    private String message;
    public DeficiencyException(){}
    public DeficiencyException(String message,Object data ) {
        this.message = message;
        this.data = data;
    }

}
