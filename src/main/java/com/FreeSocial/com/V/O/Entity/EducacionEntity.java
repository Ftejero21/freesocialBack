package com.FreeSocial.com.V.O.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EDUCACION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "INSTITUCION")
    private String institucion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO")
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN")
    private Date fechaFin;

    @Column(name = "TITULO")
    private  String titulo;

    @Column(name = "NOTA")
    private float nota;

    @Column(name = "DISCIPLINA_ACADEMICA")
    private String disciplinaAcademica;

    @Column(name = "ACTIVIDADESEXTRAESCOLARES")
    private String actividadesExtraEscolares;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "habilidadesPrincipalesEducacion", columnDefinition = "TEXT")
    private String habilidadesPrincipalesEducacion;



    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;


    public void setHabilidadesPrincipalesFromList(List<String> habilidadesPrincipales){
        this.habilidadesPrincipalesEducacion = listToJson(habilidadesPrincipales);
    }

    public List<String> getHabilidadesPrincipalesAsList() {
        return jsonToList(this.habilidadesPrincipalesEducacion);
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
