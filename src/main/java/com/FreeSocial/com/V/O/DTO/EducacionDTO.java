package com.FreeSocial.com.V.O.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducacionDTO {

    private Long id;

    private String institucion;

    private Date fechaInicio;

    private Date fechaFin;

    private float nota;

    private  String titulo;

    private String disciplinaAcademica;

    private String actividadesExtraEscolares;

    private String descripcion;

    private List<String> habilidadesPrincipalesEducacion;

}
