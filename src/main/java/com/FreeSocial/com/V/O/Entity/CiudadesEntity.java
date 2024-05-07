package com.FreeSocial.com.V.O.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CIUDADES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CiudadesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CIUDAD")
    private String ciudad;
}
