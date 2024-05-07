package com.FreeSocial.com.Service.ExperienciaLaboralService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.ExperienciaLaboralRepositorio;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.ExperienciaLaboralDTO;
import com.FreeSocial.com.V.O.Entity.EducacionEntity;
import com.FreeSocial.com.V.O.Entity.ExperienciaLaboralEntity;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class ExperienciaLaboralServiceImpl implements ExperienciaLaboralService{

    @Autowired
    private Mapping mapping;

    @Autowired
    private ExperienciaLaboralRepositorio experienciaLaboralRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public ExperienciaLaboralDTO agregarExperiencia(ExperienciaLaboralDTO experienciaLaboralDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null) {
                ExperienciaLaboralEntity experiencia = null;

                if (experienciaLaboralDTO.getId() == null) {
                    experiencia = new ExperienciaLaboralEntity();
                } else {
                    experiencia = experienciaLaboralRepositorio.findById(experienciaLaboralDTO.getId()).orElse(null);
                }

                if (experiencia != null) {
                    experiencia.setUsuario(usuario);
                    experiencia.setModoEmpleo(experienciaLaboralDTO.getModoEmpleo());
                    experiencia.setFechaInicio(experienciaLaboralDTO.getFechaInicio());
                    experiencia.setFechaFin(experienciaLaboralDTO.getFechaFin());
                    experiencia.setCargo(experienciaLaboralDTO.getCargo());
                    experiencia.setSector(experienciaLaboralDTO.getSector());
                    experiencia.setNombreEmpresa(experienciaLaboralDTO.getNombreEmpresa());
                    experiencia.setTipoEmpleo(experienciaLaboralDTO.getTipoEmpleo());
                    experiencia.setDescripcion(experienciaLaboralDTO.getDescripcion());
                    experiencia.setUbicacion(experienciaLaboralDTO.getUbicacion());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String habilidadesJson = objectMapper.writeValueAsString(experienciaLaboralDTO.getHabilidadesPrincipalesExperiencia());
                        experiencia.setHabilidadesPrincipalesExperiencia(habilidadesJson);
                    } catch (JsonProcessingException e) {
                        // Manejar la excepción, posiblemente lanzar una excepción de runtime o lógica de negocio
                        throw new RuntimeException("Error al serializar habilidades", e);
                    }

                    // Calcula el tiempo trabajado aquí y lo asignas al DTO
                    String tiempoTrabajado = calcularTiempoTrabajado(
                            experiencia.getFechaInicio(), experiencia.getFechaFin());
                    experiencia.setTiempoTrabajado(tiempoTrabajado);

                    experienciaLaboralRepositorio.save(experiencia);

                    ExperienciaLaboralDTO dtoResultante = mapping.convertirAExperienciaLaboralDTO(experiencia);
                    dtoResultante.setTiempoTrabajado(tiempoTrabajado); // Asegúrate de que el tiempo trabajado se establece en el DTO resultante

                    return dtoResultante;
                }
            }
        }
        return null;
    }

    @Override
    public List<ExperienciaLaboralDTO> obtenerExperienciaDelUsuario() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            List<ExperienciaLaboralEntity> experiencias = experienciaLaboralRepositorio.findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(userId);

            return experiencias.stream().map(experiencia -> {
                ExperienciaLaboralDTO dto = Mapping.convertirAExperienciaLaboralDTO(experiencia);

                // Convierte java.sql.Date a java.util.Date, luego a LocalDate
                LocalDate fechaInicio = new java.util.Date(experiencia.getFechaInicio().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fechaFin = experiencia.getFechaFin() != null ? new java.util.Date(experiencia.getFechaFin().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();

                // Calcula la duración en meses y luego en años y meses
                long mesesTotales = ChronoUnit.MONTHS.between(fechaInicio, fechaFin);
                long años = mesesTotales / 12;
                long meses = mesesTotales % 12;

                // Configura el tiempo trabajado
                String tiempoTrabajado = (años > 0 ? años + " " + (años == 1 ? "año" : "años") : "") +
                        (meses > 0 ? (años > 0 ? " y " : "") + meses + " " + (meses == 1 ? "mes" : "meses") : "");

                dto.setTiempoTrabajado(tiempoTrabajado);

                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ExperienciaLaboralDTO> obtenerExperienciaDeOtroUsuario(Long userId) {
            List<ExperienciaLaboralEntity> experiencias = experienciaLaboralRepositorio.findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(userId);
            if(experiencias.size() >= 1) {
                return experiencias.stream().map(experiencia -> {
                    ExperienciaLaboralDTO dto = Mapping.convertirAExperienciaLaboralDTO(experiencia);

                    // Convierte java.sql.Date a java.util.Date, luego a LocalDate
                    LocalDate fechaInicio = new java.util.Date(experiencia.getFechaInicio().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate fechaFin = experiencia.getFechaFin() != null ? new java.util.Date(experiencia.getFechaFin().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();

                    // Calcula la duración en meses y luego en años y meses
                    long mesesTotales = ChronoUnit.MONTHS.between(fechaInicio, fechaFin);
                    long años = mesesTotales / 12;
                    long meses = mesesTotales % 12;

                    // Configura el tiempo trabajado
                    String tiempoTrabajado = (años > 0 ? años + " " + (años == 1 ? "año" : "años") : "") +
                            (meses > 0 ? (años > 0 ? " y " : "") + meses + " " + (meses == 1 ? "mes" : "meses") : "");

                    dto.setTiempoTrabajado(tiempoTrabajado);

                    return dto;
                }).collect(Collectors.toList());
            }else{
                return new ArrayList<>();
            }
    }



    @Override
    public void borrarExperiencia(Long id) throws Exception {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);

            // Comprueba si la entrada de educación pertenece al usuario
            Optional<ExperienciaLaboralEntity> educacion = experienciaLaboralRepositorio.findByIdAndUsuarioId(id, userId);
            if (!educacion.isPresent()) {
                throw new Exception("Educación no encontrada o no pertenece al usuario");
            }else {
                // Si la comprobación es exitosa, procede a borrar
                experienciaLaboralRepositorio.deleteById(id);
            }

        } else {
            throw new Exception("Token no válido o ausente");
        }
    }

    public String calcularTiempoTotalTrabajado(Long userId) {
        List<ExperienciaLaboralEntity> experiencias = experienciaLaboralRepositorio.findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(userId);

        long totalMeses = experiencias.stream()
                .mapToLong(experiencia -> {
                    LocalDate fechaInicio = new java.util.Date(experiencia.getFechaInicio().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate fechaFin = experiencia.getFechaFin() != null ? new java.util.Date(experiencia.getFechaFin().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();

                    return ChronoUnit.MONTHS.between(fechaInicio, fechaFin);
                })
                .sum();

        long años = totalMeses / 12;
        long meses = totalMeses % 12;

        String tiempoTrabajado = (años > 0 ? años + " " + (años == 1 ? "año" : "años") : "") +
                (meses > 0 ? (años > 0 ? " y " : "") + meses + " " + (meses == 1 ? "mes" : "meses") : "");

        return tiempoTrabajado;
    }

    @Override
    public String obtenerTiempoTotalTrabajadoDelUsuario() {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            return calcularTiempoTotalTrabajado(userId);
        }
        return "No se pudo calcular el tiempo total trabajado";
    }

    private String calcularTiempoTrabajado(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null) {
            return "Desconocido o en curso";
        }

        LocalDate inicio = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = fechaFin != null ? fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();

        long mesesTotales = ChronoUnit.MONTHS.between(inicio, fin);
        long años = mesesTotales / 12;
        long meses = mesesTotales % 12;

        return (años > 0 ? años + " año" + (años > 1 ? "s" : "") : "") +
                (meses > 0 ? (años > 0 ? " y " : "") + meses + " mes" + (meses > 1 ? "es" : "") : "");
    }
}
