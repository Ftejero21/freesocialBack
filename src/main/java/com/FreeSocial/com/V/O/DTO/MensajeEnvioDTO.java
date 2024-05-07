package com.FreeSocial.com.V.O.DTO;

import lombok.Data;

@Data
public class MensajeEnvioDTO {

    private Long receptorId;
    private String contenido;
    private String imagenMensajeBase64;
}
