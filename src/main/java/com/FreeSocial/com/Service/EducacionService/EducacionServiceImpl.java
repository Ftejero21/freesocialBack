package com.FreeSocial.com.Service.EducacionService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.EducacionRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.EducacionDTO;
import com.FreeSocial.com.V.O.Entity.EducacionEntity;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EducacionServiceImpl implements EducacionService{


    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EducacionRepository educacionRepository;


    @Autowired
    private Mapping mapping;

    @Override
    public EducacionDTO agregarEducacion(EducacionDTO educacionDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null) {
                EducacionEntity educacion;
                if (educacionDTO.getId() == null) {
                    // Crear nueva educación
                    educacion = new EducacionEntity();
                } else {
                    // Actualizar educación existente
                    educacion = educacionRepository.findById(educacionDTO.getId()).orElse(null);
                }

                // Configurar la entidad EducacionEntity con los datos de educacionDTO
                educacion.setInstitucion(educacionDTO.getInstitucion());
                educacion.setFechaInicio(educacionDTO.getFechaInicio());
                educacion.setFechaFin(educacionDTO.getFechaFin());
                educacion.setTitulo(educacionDTO.getTitulo());
                educacion.setDisciplinaAcademica(educacionDTO.getDisciplinaAcademica());
                educacion.setActividadesExtraEscolares(educacionDTO.getActividadesExtraEscolares());
                educacion.setDescripcion(educacionDTO.getDescripcion());
                educacion.setNota(educacionDTO.getNota());
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String habilidadesJson = objectMapper.writeValueAsString(educacionDTO.getHabilidadesPrincipalesEducacion());
                    educacion.setHabilidadesPrincipalesEducacion(habilidadesJson);
                } catch (JsonProcessingException e) {
                    // Manejar la excepción, posiblemente lanzar una excepción de runtime o lógica de negocio
                    throw new RuntimeException("Error al serializar habilidades", e);
                }
                educacion.setUsuario(usuario);

                // Guardar o actualizar la entidad
                educacionRepository.save(educacion);

                return mapping.convertirAeducacionDTO(educacion);
            }
        }
        return null;

    }

    @Override
    public List<EducacionDTO> obtenerEducacionesDelUsuario() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            List<EducacionEntity> educaciones = educacionRepository.findByUsuarioIdOrderByFechaFinDescFechaInicioAsc(userId);
            // Ahora recolectamos los resultados en una List en lugar de un Set para mantener el orden.
            return educaciones.stream()
                    .map(Mapping::convertirAeducacionDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<EducacionDTO> obtenerEducacionesDeOtroUsuario(Long userId) {
        List<EducacionEntity> educaciones = educacionRepository.findByUsuarioIdOrderByFechaFinDescFechaInicioAsc(userId);
        if (educaciones.size() >= 1) {

            // Ahora recolectamos los resultados en una List en lugar de un Set para mantener el orden.
            return educaciones.stream()
                    .map(Mapping::convertirAeducacionDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void borrarEducacion(Long id) throws Exception {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);

            // Comprueba si la entrada de educación pertenece al usuario
            Optional<EducacionEntity> educacion = educacionRepository.findByIdAndUsuarioId(id, userId);
            if (!educacion.isPresent()) {
                throw new Exception("Educación no encontrada o no pertenece al usuario");
            }else {
                // Si la comprobación es exitosa, procede a borrar
                educacionRepository.deleteById(id);
            }

        } else {
            throw new Exception("Token no válido o ausente");
        }
    }



}
