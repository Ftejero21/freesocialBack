package com.FreeSocial.com.V.O.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CONTRATANTE_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContratanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "EMPRESA")
    private String empresa;

    @OneToOne(mappedBy = "contratanteData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UsuarioEntity usuario;
}
