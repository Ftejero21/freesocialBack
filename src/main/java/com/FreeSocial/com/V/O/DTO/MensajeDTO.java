package com.FreeSocial.com.V.O.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class MensajeDTO {

    private Long id;
    private Long userId;
    private String fechaCreacion;
    private String contenido;
    private String imagenMensajeBase64;  // Representación en base64
    private byte[] imagenMensaje;
    private Long chatId;
    private boolean leido;
}
