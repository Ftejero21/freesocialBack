package com.FreeSocial.com.V.O.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DatosPersonales")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosPersonalesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDOS")
    private String apellidos;

    @Column(name = "TELEFONO")
    private String telefono;


    @Column(name = "FechaNacimiento")
    private Date fechaNacimiento;

    @Column(name = "CIUDAD")
    private String ciudad;

    @OneToOne(mappedBy = "datosPersonales", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UsuarioEntity usuario;
}
