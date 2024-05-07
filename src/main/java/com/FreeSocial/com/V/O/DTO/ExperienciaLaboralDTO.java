package com.FreeSocial.com.V.O.DTO;

import lombok.Data;


import java.util.Date;
import java.util.List;

@Data
public class ExperienciaLaboralDTO {

    private Long id;

    private String cargo;

    private String tipoEmpleo;

    private String sector;

    private String nombreEmpresa;

    private String ubicacion;

    private String modoEmpleo;

    private Date fechaInicio;

    private Date fechaFin;

    private String descripcion;

    private List<String> habilidadesPrincipalesExperiencia;

    private String tiempoTrabajado;

}
