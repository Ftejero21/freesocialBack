package com.FreeSocial.com.V.O.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ComentarioDTO {

    private Long id;
    private String texto;
    private String nombreCompleto;
    private String fechaCreacion;

    private Long userId;
    private String imagenPerfilBase64;
    private String titular;

    private Long comentarioPadreId;
    private List<ComentarioDTO> respuestas;  // Lista de respuestas
    public ComentarioDTO(Long id,String texto, String nombreCompleto, String fechaCreacion, String imagenPerfilBase64, String titular,Long userId) {
        this.id = id;
        this.texto = texto;
        this.nombreCompleto = nombreCompleto;
        this.fechaCreacion = fechaCreacion;
        this.imagenPerfilBase64 = imagenPerfilBase64;
        this.titular = titular;
        this.userId = userId;
    }
}
