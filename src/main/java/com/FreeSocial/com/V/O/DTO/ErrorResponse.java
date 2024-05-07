package com.FreeSocial.com.V.O.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 5327712965228532149L;

    private Date timestamp;

    private int code;

    private String status;

    private Set<String> messages = new HashSet<>();

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(HttpStatus httpStatus, Set<String> messages) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.messages = messages;
    }

    @JsonIgnore
    public void addMessages(Set<String> messages) {
        this.messages.addAll(messages);
    }
}
