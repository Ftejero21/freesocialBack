package com.FreeSocial.com.V.O.DTO;

import com.FreeSocial.com.V.O.Entity.ContratanteEntity;
import com.FreeSocial.com.V.O.Entity.DatosPersonalesEntity;
import com.FreeSocial.com.V.O.Entity.FreelancerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {
    private Long id;
    private String email;
    private String password;
    private ContratanteDataDTO contratanteData;
    private FreelancerDataDTO freelancerData;
    private Set<RolDTO> roles;
    private Set<EducacionDTO> educacion;
    private Set<ProyectosDTO> proyectos;
    private Set<PublicacionDTO> publicaciones;
    private Integer aparicionBusqueda;
    private Set<ExperienciaLaboralDTO> experiencia;
    private DatosPersonalesDTO datosPersonales;

    private Boolean recordado;
    private String imagenPerfilBase64;  // Representación en base64
    private byte[] imagenPerfil;        // Representación en bytes
    private String imagenBackgroundBase64;  // Representación en base64
    private byte[] imagenBackground;

    private Date fechaCreacion;

    private LocalDateTime ultimoLogin;

    private String descripcion;

    private String titular;

    public String getImagenPerfilBase64() {
        return imagenPerfilBase64;
    }

    public Boolean getRecordado() {
        return recordado == null ? Boolean.FALSE : recordado;
    }

    public byte[] getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(byte[] imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getImagenBackgroundBase64() {
        return imagenBackgroundBase64;
    }

    public byte[] getImagenBackground() {
        return imagenBackground;
    }

    public void setImagenBackground(byte[] imagenBackground) {
        this.imagenBackground = imagenBackground;
    }



}
