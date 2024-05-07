package com.FreeSocial.com.Config;

import com.FreeSocial.com.Exception.FreeSocialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FreeSocialException.class)
    public ResponseEntity<String> handleFreeSocialException(FreeSocialException e) {
        // Aquí puedes decidir qué código de estado y mensaje enviar. Por simplicidad, usaré BAD_REQUEST como ejemplo.
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
