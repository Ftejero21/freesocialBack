package com.FreeSocial.com.V.O.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatDTO {

    private Long id;
    private Long userId;
    private Long receptorId;
    private String nombreCompletoReceptor;
    private String imagenPerfilReceptorBase64;
    private String titularReceptor;
    private LocalDateTime fechaCreacion;
    private List<MensajeDTO> mensajes;
}
