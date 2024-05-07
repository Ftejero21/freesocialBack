package com.FreeSocial.com.V.O.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    @Column(name = "USER_ID")
    private Long userId;

    @Lob
    @Column(name = "IMAGEN_MENSAJE", columnDefinition="BLOB")
    private byte[] imagenMensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "leido", nullable = false)
    private boolean leido = false;

    public boolean getLeido() {
        return this.leido;
    }
}