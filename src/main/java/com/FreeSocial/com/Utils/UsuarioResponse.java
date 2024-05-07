package com.FreeSocial.com.Utils;

import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {
    private UsuarioDTO usuarioDTO;
    private String token;
}
