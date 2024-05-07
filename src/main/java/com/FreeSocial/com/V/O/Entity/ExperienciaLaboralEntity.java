package com.FreeSocial.com.V.O.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "EXPERIENCIALABORAL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExperienciaLaboralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CARGO")
    private String cargo;

    @Column(name = "TIPO_EMPLEO")
    private String tipoEmpleo;

    @Column(name = "SECTOR")
    private String sector;

    @Column(name = "NombreEmpresa")
    private String nombreEmpresa;

    @Column(name = "UBICACION")
    private String ubicacion;

    @Column(name = "MODOEMPLEO")
    private String modoEmpleo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO")
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN")
    private Date fechaFin;

    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "habilidadesPrincipalesExperiencia", columnDefinition = "TEXT")
    private String habilidadesPrincipalesExperiencia;

    @Column(name = "TIEMPOTRABAJADO")
    private String tiempoTrabajado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    public void setHabilidadesPrincipalesFromList(List<String> habilidadesPrincipales){
        this.habilidadesPrincipalesExperiencia = listToJson(habilidadesPrincipales);
    }

    public List<String> getHabilidadesPrincipalesAsList() {
        return jsonToList(this.habilidadesPrincipalesExperiencia);
    }

    private static List<String> jsonToList(String json) {

        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON a lista", e);
        }
    }

    private static String listToJson(List<String> list) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir lista a JSON", e);
        }
    }
}
