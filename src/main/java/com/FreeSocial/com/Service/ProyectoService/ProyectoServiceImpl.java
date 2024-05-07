package com.FreeSocial.com.Service.ProyectoService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.ProyectoRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import com.FreeSocial.com.V.O.DTO.ProyectosDTO;
import com.FreeSocial.com.V.O.Entity.ExperienciaLaboralEntity;
import com.FreeSocial.com.V.O.Entity.ProyectosEntity;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoServiceImpl implements ProyectoService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private Mapping mapping;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    public ProyectosDTO agregarProyecto(ProyectosDTO proyectosDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null) {
                ProyectosEntity proyecto = null;

                if (proyectosDTO.getId() == null) {
                    proyecto = new ProyectosEntity();
                } else {
                    proyecto = proyectoRepository.findById(proyectosDTO.getId()).orElse(null);
                }

                if (proyecto != null) {
                    proyecto.setUsuario(usuario);
                    proyecto.setFechaInicio(proyectosDTO.getFechaInicio());
                    proyecto.setFechaFin(proyectosDTO.getFechaFin());
                    proyecto.setLink(proyectosDTO.getLink());
                    if (proyectosDTO.getImagenProyectoBase64()!= null && !proyectosDTO.getImagenProyectoBase64().isEmpty()) {
                        byte[] imagenProyectoBytes = Base64.getDecoder().decode(proyectosDTO.getImagenProyectoBase64());
                        proyecto.setImagenProyecto(imagenProyectoBytes);
                    }
                    proyecto.setTitulo(proyectosDTO.getTitulo());
                    proyecto.setDescripcion(proyectosDTO.getDescripcion());

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String habilidadesJson = objectMapper.writeValueAsString(proyectosDTO.getHabilidadesPrincipalesProyecto());
                        proyecto.setHabilidadesPrincipalesProyecto(habilidadesJson);
                    } catch (JsonProcessingException e) {
                        // Manejar la excepción, posiblemente lanzar una excepción de runtime o lógica de negocio
                        throw new RuntimeException("Error al serializar habilidades", e);
                    }


                    proyectoRepository.save(proyecto);

                    ProyectosDTO dtoResultante = mapping.convertirAProyectosDTO(proyecto);

                    return dtoResultante;
                }
            }
        }
        return null;
    }

    @Override
    public List<ProyectosDTO> obtenerProyectosDelUsuario() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            List<ProyectosEntity> proyectos = proyectoRepository.findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(userId);

            return proyectos.stream().map(proyecto -> {
                ProyectosDTO dto = Mapping.convertirAProyectosDTO(proyecto);
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProyectosDTO> obtenerProyectosDeOtroUsuario(Long userId) {
        List<ProyectosEntity> proyectos = proyectoRepository.findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(userId);
        if (proyectos.size() >= 1) {
            return proyectos.stream().map(proyecto -> {
                ProyectosDTO dto = Mapping.convertirAProyectosDTO(proyecto);
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
