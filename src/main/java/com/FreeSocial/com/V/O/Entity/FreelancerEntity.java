package com.FreeSocial.com.V.O.Entity;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FREELANCER_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "habilidades", columnDefinition = "TEXT")
    private String habilidades; // Almacenamos el JSON como String

    @Column(name = "habilidadesPrincipales", columnDefinition = "TEXT")
    private String habilidadesPrincipales; // Almacenamos el JSON como String

    @Column(name = "intereses", columnDefinition = "TEXT")
    private String intereses; // Almacenamos el JSON como String


    @OneToOne(mappedBy = "freelancerData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UsuarioEntity usuario;

    // Métodos auxiliares para manejar habilidades como lista
    public List<String> getHabilidadesAsList() {
        return jsonToList(this.habilidades);
    }

    public List<String> getHabilidadesPrincipalesAsList() {
        return jsonToList(this.habilidadesPrincipales);
    }

    public void setHabilidadesFromList(List<String> habilidades) {
        this.habilidades = listToJson(habilidades);
    }

    public void setHabilidadesPrincipalesFromList(List<String> habilidadesPrincipales){
        this.habilidadesPrincipales = listToJson(habilidadesPrincipales);
    }

    // Métodos auxiliares para manejar intereses como lista
    public List<String> getInteresesAsList() {
        return jsonToList(this.intereses);
    }

    public void setInteresesFromList(List<String> intereses) {
        this.intereses = listToJson(intereses);
    }

    // Utilizando Jackson para la conversión
    private static String listToJson(List<String> list) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir lista a JSON", e);
        }
    }

    private static List<String> jsonToList(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON a lista", e);
        }
    }
}
