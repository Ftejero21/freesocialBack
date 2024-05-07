package com.FreeSocial.com.V.O.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.websocket.server.ServerEndpoint;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
    private Boolean recordado;
}
