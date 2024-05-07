package com.FreeSocial.com.V.O.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "USUARIO")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob
    @Column(name = "IMAGEN_PERFIL", columnDefinition="BLOB")
    private byte[] imagenPerfil;

    @Lob
    @Column(name = "IMAGEN_BACKGROUND", columnDefinition="BLOB")
    private byte[] imagenBackground;

    @Column(name = "CODIGO_RECUPERACION")
    private String codigoRecuperacion;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "titular")
    private String titular;

    @Column(name = "FECHA_EXPIRACION_CODIGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracionCodigoRecuperacion;


    @Column(name = "EMAIL",unique = true)
    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "freelancerDataId")
    private FreelancerEntity freelancerData;

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private Set<EducacionEntity> educacion;

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private Set<ExperienciaLaboralEntity> experiencia;

    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private Set<ProyectosEntity> proyectos;

    @Column(name = "FechaCreacion")
    private Date fechaCreacion;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;


    @Column(name = "APARICIONBUSQUEDA")
    private Integer aparicionBusqueda = 0;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "contratanteDataId")
    private ContratanteEntity contratanteData;

    @Column(nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE" , name = "RECORDADO")
    private Boolean recordado = false;

    @Column(name = "PASSWORD")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RolEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Publicacion> publicaciones;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "datosPersonalesId")
    private DatosPersonalesEntity datosPersonales;

    @ManyToMany
    @JoinTable(name = "seguidores",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "seguidor_id"))
    private Set<UsuarioEntity> seguidores = new HashSet<>();

    @ManyToMany(mappedBy = "seguidores")
    private Set<UsuarioEntity> siguiendo = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatEntity> chats;


    public UsuarioEntity(Long id) {
        this.id = id;
    }
}
