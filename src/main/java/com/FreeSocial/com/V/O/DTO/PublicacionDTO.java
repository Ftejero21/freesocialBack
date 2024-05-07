package com.FreeSocial.com.V.O.DTO;


import com.FreeSocial.com.V.O.Entity.Comentario;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublicacionDTO {

    private Long id;
    private String titulo;
    private String texto;
    private String fechaPublicacion;
    private String imagenPerfil; // Tipo String en lugar de byte[]
    private String nombre;
    private String apellido;
    private String titular;
    private boolean liked;

    private boolean sigueAutor;



    private Long idUser;
    private String imagenPublicacionBase64;

    private byte[] imagenPublicacion;
    private List<ComentarioDTO> comentarios;

    private UsuarioDTO usuario;
    private Integer likeCount;

    public PublicacionDTO(Long id, String titulo, String texto, String fechaPublicacion,
                          String imagenPerfil, String nombre, String apellido,
                          String titular, boolean liked, Integer likeCount, List<ComentarioDTO> comentarioDTOS, String imagenPublicacionBase64, Long idUser,UsuarioDTO user) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenPerfil = imagenPerfil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.titular = titular;
        this.liked = liked;
        this.likeCount = likeCount;
        this.comentarios = comentarioDTOS;
        this.imagenPublicacionBase64 = imagenPublicacionBase64;
        this.idUser = idUser;

        this.usuario = user;


    }


}
