package com.FreeSocial.com.V.O.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProyectosDTO {

    private Long id;

    private  String titulo;

    private String descripcion;

    private String link;

    private Date fechaInicio;

    private Date fechaFin;

    private List<String> habilidadesPrincipalesProyecto;

    private String imagenProyectoBase64;

    private byte[] imagenProyecto;
}
