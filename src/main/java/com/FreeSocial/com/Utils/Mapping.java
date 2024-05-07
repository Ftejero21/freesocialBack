package com.FreeSocial.com.Utils;

import com.FreeSocial.com.Exception.FreeSocialException;
import com.FreeSocial.com.Repository.EducacionRepository;
import com.FreeSocial.com.Repository.RolRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.V.O.DTO.*;
import com.FreeSocial.com.V.O.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Mapping {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CryptComponent cryptComponent;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EducacionRepository educacionRepository;

    public  UsuarioEntity toUsuarioEntity(UsuarioDTO usuarioDTO) {
        UsuarioEntity usuario;
        usuario = usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(() -> new FreeSocialException("error.user.notFound"));

        FreelancerEntity freelancerEntity = null;
        ContratanteEntity contratanteEntity = null;


        if (usuarioDTO.getTitular() != null || !usuarioDTO.getTitular().isEmpty()) {
            usuario.setTitular(usuarioDTO.getTitular());
        } else {
            usuario.setTitular(null);
        }

        if (usuarioDTO.getDescripcion() != null) {
            usuario.setDescripcion(usuarioDTO.getDescripcion());
        }else {
            usuario.setDescripcion(null);
        }


        if (usuarioDTO.getImagenPerfilBase64() != null && !usuarioDTO.getImagenPerfilBase64().isEmpty()) {
            try {
                byte[] imagenPerfilBytes = Base64.getDecoder().decode(usuarioDTO.getImagenPerfilBase64());
                usuario.setImagenPerfil(imagenPerfilBytes);
            } catch (IllegalArgumentException e) {
                // Manejar la excepción si la cadena base64 no es válida
                throw new FreeSocialException("error.invalidImagePerfilBase64");
            }
        } else {
            usuario.setImagenPerfil(null);
        }

        if (usuarioDTO.getImagenBackgroundBase64() != null && !usuarioDTO.getImagenBackgroundBase64().isEmpty()) {
            try {
                byte[] imagenBackgroundBytes = Base64.getDecoder().decode(usuarioDTO.getImagenBackgroundBase64());
                usuario.setImagenBackground(imagenBackgroundBytes);
            } catch (IllegalArgumentException e) {
                // Manejar la excepción si la cadena base64 no es válida
                throw new FreeSocialException("error.invalidImageBackgroundBase64");
            }
        } else {
            usuario.setImagenBackground(null);
        }

        // Actualización de roles
            Set<RolEntity> rolesEntity = new HashSet<>();
            for (RolDTO rolDTO : usuarioDTO.getRoles()) {
                RolEntity rolEntity = rolRepository.findById(rolDTO.getId()).orElse(null);
                if (rolEntity != null) {
                    rolesEntity.add(rolEntity);

                    if (rolDTO.getId().equals(FreeSocialConstants.ROL_FREELANCER) && usuarioDTO.getFreelancerData() != null) {
                        FreelancerDataDTO freelancerDTO = usuarioDTO.getFreelancerData();
                        if (usuario.getFreelancerData() == null) {
                            freelancerEntity = new FreelancerEntity();
                            usuario.setFreelancerData(freelancerEntity);
                        } else {
                            freelancerEntity = usuario.getFreelancerData();
                        }
                        freelancerEntity.setHabilidadesFromList(freelancerDTO.getHabilidades());
                        freelancerEntity.setHabilidadesPrincipalesFromList(freelancerDTO.getHabilidadesPrincipales());
                        freelancerEntity.setInteresesFromList(freelancerDTO.getIntereses());
                        // Asegúrate de establecer la relación bidireccional si es necesario
                        freelancerEntity.setUsuario(usuario);
                    } else if (rolDTO.getId().equals(FreeSocialConstants.ROL_CONTRATANTE) && usuarioDTO.getContratanteData() != null) {
                        ContratanteDataDTO contratanteDTO = usuarioDTO.getContratanteData();

                        // Verifica si ya existe una entidad de Contratante asociada
                        if (usuario.getContratanteData() == null) {
                            contratanteEntity = new ContratanteEntity();
                            contratanteEntity.setUsuario(usuario);
                            usuario.setContratanteData(contratanteEntity);
                        } else {
                            contratanteEntity = usuario.getContratanteData();

                        }

                        contratanteEntity.setEmpresa(contratanteDTO.getEmpresa());


                    }
                }
            }
            usuario.setRoles(rolesEntity);

            // Actualización de datos personales
            if (usuarioDTO.getDatosPersonales() != null) {
                DatosPersonalesDTO datosDTO = usuarioDTO.getDatosPersonales();
                DatosPersonalesEntity datosEntity = usuario.getDatosPersonales();

                if (datosEntity == null) {
                    datosEntity = new DatosPersonalesEntity();
                }
                datosEntity.setNombre(datosDTO.getNombre());
                datosEntity.setApellidos(datosDTO.getApellidos());
                datosEntity.setTelefono(datosDTO.getTelefono());
                datosEntity.setFechaNacimiento(datosDTO.getFechaNacimiento());
                datosEntity.setCiudad(datosDTO.getCiudad());

                usuario.setDatosPersonales(datosEntity);
            }
            return usuario;
        }


    public static  EducacionDTO convertirAeducacionDTO(EducacionEntity educacion) {
        EducacionDTO dto = new EducacionDTO();
        dto.setId(educacion.getId());
        dto.setInstitucion(educacion.getInstitucion());
        dto.setFechaInicio(educacion.getFechaInicio());
        dto.setFechaFin(educacion.getFechaFin());
        dto.setTitulo(educacion.getTitulo());
        dto.setDisciplinaAcademica(educacion.getDisciplinaAcademica());
        dto.setActividadesExtraEscolares(educacion.getActividadesExtraEscolares());
        dto.setDescripcion(educacion.getDescripcion());
        dto.setHabilidadesPrincipalesEducacion(educacion.getHabilidadesPrincipalesAsList());
        dto.setNota(educacion.getNota());
        return dto;
    }

    public static  ExperienciaLaboralDTO convertirAExperienciaLaboralDTO(ExperienciaLaboralEntity experiencia) {
        ExperienciaLaboralDTO dto = new ExperienciaLaboralDTO();
        dto.setId(experiencia.getId());
        dto.setCargo(experiencia.getCargo());
        dto.setSector(experiencia.getSector());
        dto.setFechaInicio(experiencia.getFechaInicio());
        dto.setFechaFin(experiencia.getFechaFin());
        dto.setTipoEmpleo(experiencia.getTipoEmpleo());
        dto.setModoEmpleo(experiencia.getModoEmpleo());
        dto.setNombreEmpresa(experiencia.getNombreEmpresa());
        dto.setDescripcion(experiencia.getDescripcion());
        dto.setUbicacion(experiencia.getUbicacion());
        dto.setTiempoTrabajado(experiencia.getTiempoTrabajado());
        dto.setHabilidadesPrincipalesExperiencia(experiencia.getHabilidadesPrincipalesAsList());
        return dto;
    }

    public static PublicacionDTO convertirAPublicacionDTO(Publicacion publicacion) {
        if (publicacion == null) {
            return null;
        }

        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setTitulo(publicacion.getTitulo());
        dto.setTexto(publicacion.getTexto());  // Asumiendo que en la entidad el campo se llama 'texto'

        // Convierte fechaPublicacion a una descripción del tiempo transcurrido
        String tiempoDesdePublicacion = calculateTimeSince(publicacion.getFechaPublicacion());
        dto.setFechaPublicacion(tiempoDesdePublicacion);

        return dto;
    }

    // Asegúrate de que calculateTimeSince esté accesible y adecuadamente definida
    public static String calculateTimeSince(LocalDateTime fechaPublicacion) {
        LocalDateTime now = LocalDateTime.now();
        long years = ChronoUnit.YEARS.between(fechaPublicacion, now);
        if (years > 0) {
            return fechaPublicacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        long days = ChronoUnit.DAYS.between(fechaPublicacion, now);
        if (days > 30) {
            long months = ChronoUnit.MONTHS.between(fechaPublicacion, now);
            return "Hace " + months + (months == 1 ? " mes" : " meses");
        } else if (days > 0) {
            return "Hace " + days + (days == 1 ? " día" : " días");
        }

        long hours = ChronoUnit.HOURS.between(fechaPublicacion, now);
        if (hours > 0) {
            return "Hace " + hours + (hours == 1 ? " hora" : " horas");
        }

        long minutes = ChronoUnit.MINUTES.between(fechaPublicacion, now);
        if (minutes > 0) {
            return "Hace " + minutes + (minutes == 1 ? " minuto" : " minutos");
        }

        return "Hace unos momentos";
    }

    public static ProyectosDTO convertirAProyectosDTO(ProyectosEntity proyecto) {
        ProyectosDTO dto = new ProyectosDTO();
        dto.setId(proyecto.getId());
        dto.setTitulo(proyecto.getTitulo());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setFechaInicio(proyecto.getFechaInicio());
        dto.setFechaFin(proyecto.getFechaFin());
        dto.setLink(proyecto.getLink());
        dto.setHabilidadesPrincipalesProyecto(proyecto.getHabilidadesPrincipalesAsList());
        dto.setImagenProyecto(proyecto.getImagenProyecto());

        return dto;
    }

    public static UsuarioDTO toUsuarioDTO(UsuarioEntity entity) {
        UsuarioDTO dto = new UsuarioDTO();

        // Mapeo básico de atributos
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setImagenPerfil(entity.getImagenPerfil());
        dto.setImagenBackground(entity.getImagenBackground());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setDescripcion(entity.getDescripcion());
        dto.setTitular(entity.getTitular());
        dto.setAparicionBusqueda(entity.getAparicionBusqueda());
        // Mapeo de DatosPersonales
        DatosPersonalesEntity datosPersonalesEntity = entity.getDatosPersonales();
        if (datosPersonalesEntity != null) {
            DatosPersonalesDTO datosDTO = new DatosPersonalesDTO();
            datosDTO.setNombre(datosPersonalesEntity.getNombre());
            datosDTO.setApellidos(datosPersonalesEntity.getApellidos());
            datosDTO.setTelefono(datosPersonalesEntity.getTelefono());
            datosDTO.setFechaNacimiento(datosPersonalesEntity.getFechaNacimiento());
            datosDTO.setCiudad(datosPersonalesEntity.getCiudad());
            dto.setDatosPersonales(datosDTO);
        }

        Set<EducacionEntity> educacionEntities = entity.getEducacion();
        Set<String> habilidadesPrincipales = new HashSet<>();
        if (educacionEntities != null) {
            Set<EducacionDTO> educacionDTOs = educacionEntities.stream()
                    .sorted(Comparator.comparing(EducacionEntity::getId).reversed())
                    .map(educacionEntity -> {
                        EducacionDTO educacionDTO = new EducacionDTO();
                        educacionDTO.setId(educacionEntity.getId());
                        educacionDTO.setInstitucion(educacionEntity.getInstitucion());
                        educacionDTO.setFechaInicio(educacionEntity.getFechaInicio());
                        educacionDTO.setFechaFin(educacionEntity.getFechaFin());
                        educacionDTO.setDescripcion(educacionEntity.getDescripcion());
                        educacionDTO.setActividadesExtraEscolares(educacionEntity.getActividadesExtraEscolares());
                        educacionDTO.setDisciplinaAcademica(educacionEntity.getDisciplinaAcademica());
                        educacionDTO.setTitulo(educacionEntity.getTitulo());
                        educacionDTO.setHabilidadesPrincipalesEducacion(educacionEntity.getHabilidadesPrincipalesAsList());
                        return educacionDTO;
                    }).collect(Collectors.toSet());
            dto.setEducacion(educacionDTOs);
        }else{
            dto.setEducacion(null);
        }

        Set<RolEntity> rolesEntity = entity.getRoles();
        if (rolesEntity != null && !rolesEntity.isEmpty()) {
            Set<RolDTO> rolesDTO = rolesEntity.stream().map(rolEntity -> {
                RolDTO rolDTO = new RolDTO();
                rolDTO.setId(rolEntity.getId());
                rolDTO.setNombre(rolEntity.getNombre());

                if (rolEntity.getId() == 2) {
                    // Suponiendo que entity es de tipo UsuarioEntity y tiene un método getFreelancerData()
                    FreelancerEntity freelancerData = entity.getFreelancerData();
                    if (freelancerData != null) {

                        FreelancerDataDTO freelancerDTO = convertirAFreelancerDTO(freelancerData);
                        // Obtiene las habilidades actuales de FreelancerDTO.
                        List<String> habilidadesActuales = freelancerDTO.getHabilidades();
                        if (habilidadesActuales == null) {
                            habilidadesActuales = new ArrayList<>();
                        }

                        // Añade las nuevas habilidades a la lista existente.
                        habilidadesActuales.addAll(habilidadesPrincipales);

                        // Elimina duplicados si es necesario
                        Set<String> habilidadesSinDuplicados = new HashSet<>(habilidadesActuales);

                        // Establece la lista actualizada de habilidades en FreelancerDTO.
                        freelancerDTO.setHabilidades(new ArrayList<>(habilidadesSinDuplicados));

                        dto.setFreelancerData(freelancerDTO);
                    }else{
                        System.out.println("Es null");
                    }
                }

                return rolDTO;
            }).collect(Collectors.toSet());
            dto.setRoles(rolesDTO);
        }

        return dto;
    }

    private static FreelancerDataDTO convertirAFreelancerDTO(FreelancerEntity freelancerEntity) {
        FreelancerDataDTO freelancerDataDTO = new FreelancerDataDTO();

        // Utiliza los métodos auxiliares para convertir JSON a List<String>
        freelancerDataDTO.setHabilidades(freelancerEntity.getHabilidadesAsList());
        freelancerDataDTO.setHabilidadesPrincipales(freelancerEntity.getHabilidadesPrincipalesAsList());
        freelancerDataDTO.setIntereses(freelancerEntity.getInteresesAsList());

        return freelancerDataDTO;
    }



    public static DatosPersonalesDTO toDatosPersonalesDTO(DatosPersonalesEntity entity) {
        DatosPersonalesDTO dto = new DatosPersonalesDTO();
        dto.setNombre(entity.getNombre());
        dto.setApellidos(entity.getApellidos());
        dto.setTelefono(entity.getTelefono());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setCiudad(entity.getCiudad());
        // y otros campos si los hubiera
        return dto;
    }

    public static CiudadDTO convertEntityToDTO(CiudadesEntity ciudadEntity) {
        // Suponiendo que CiudadDTO tiene un constructor o método para establecer sus propiedades
        CiudadDTO ciudadDTO = new CiudadDTO();
        ciudadDTO.setCiudades(ciudadEntity.getCiudad());
        // ... establecer otras propiedades necesarias ...
        return ciudadDTO;
    }

    public static DatosPersonalesEntity toDatosPersonalesEntity(DatosPersonalesDTO dto) {
        DatosPersonalesEntity entity = new DatosPersonalesEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellidos(dto.getApellidos());
        entity.setTelefono(dto.getTelefono());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setCiudad(dto.getCiudad());
        // y otros campos si los hubiera
        return entity;
    }

    public static DatosPersonalesEntity updateDatosPersonalesFromDTO(DatosPersonalesDTO dto, DatosPersonalesEntity entity) {
        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }


        if (dto.getApellidos() != null) {
            entity.setApellidos(dto.getApellidos());
        }

        if (dto.getTelefono() != null) {
            entity.setTelefono(dto.getTelefono());
        }


        if (dto.getFechaNacimiento() != null) {
            entity.setFechaNacimiento(dto.getFechaNacimiento());
        }

        if (dto.getCiudad() != null) {
            entity.setCiudad(dto.getCiudad());
        }

        return entity;
    }
}







