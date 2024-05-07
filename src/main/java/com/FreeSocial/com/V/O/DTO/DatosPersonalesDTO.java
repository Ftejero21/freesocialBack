package com.FreeSocial.com.V.O.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosPersonalesDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private Date fechaNacimiento;
    private String ciudad;
}
