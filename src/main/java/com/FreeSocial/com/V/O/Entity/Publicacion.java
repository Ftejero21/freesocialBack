package com.FreeSocial.com.V.O.Entity;


import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Table(name = "PUBLICACION")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "texto")
    private String texto;

    @Column(name = "FECHA_PUBLICACION")
    private LocalDateTime fechaPublicacion;

    @Lob
    @Column(name = "IMAGEN_PUBLICACION", columnDefinition="BLOB")
    private byte[] imagenPublicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsuarioEntity user;


    @Column(name = "LIKECOUNT")
    private Integer likeCount = 0;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fecha_creacion DESC")
    private List<Comentario> comentarios = new ArrayList<>();


    public Publicacion(Long id) {
        this.id = id;
    }
}
