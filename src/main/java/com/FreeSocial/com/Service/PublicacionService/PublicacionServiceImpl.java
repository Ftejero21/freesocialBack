package com.FreeSocial.com.Service.PublicacionService;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.ComentarioRepository;
import com.FreeSocial.com.Repository.LikeRepository;
import com.FreeSocial.com.Repository.PublicacionRepository;
import com.FreeSocial.com.Repository.UsuarioRepository;
import com.FreeSocial.com.Service.UsuarioService.UsuarioService;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.ComentarioDTO;
import com.FreeSocial.com.V.O.DTO.FiltroDTO;
import com.FreeSocial.com.V.O.DTO.PublicacionDTO;
import com.FreeSocial.com.V.O.DTO.UsuarioDTO;
import com.FreeSocial.com.V.O.Entity.Comentario;
import com.FreeSocial.com.V.O.Entity.Publicacion;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImpl implements PublicacionService{

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public PublicacionDTO saveOrUpdatePublicacion(PublicacionDTO publicacionDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        if (token != null) {
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UsuarioEntity usuario = usuarioRepository.findById(userId).orElse(null);

            if (usuario != null) {
                Publicacion publicacion = null;

                if (publicacionDTO.getId() == null) {
                    publicacion = new Publicacion();
                } else {
                    publicacion = publicacionRepository.findById(publicacionDTO.getId()).orElse(null);
                }

                if (publicacion != null) {
                    publicacion.setUser(usuario);
                    publicacion.setLikeCount(0);
                    publicacion.setTitulo(publicacionDTO.getTitulo());
                    publicacion.setTexto(publicacionDTO.getTexto());
                    ZoneId zoneId = ZoneId.of("Europe/Madrid");
                    LocalDateTime fechaHoraMadrid = LocalDateTime.now(zoneId);
                    publicacion.setFechaPublicacion(fechaHoraMadrid);

                    if (publicacionDTO.getImagenPublicacionBase64() != null && !publicacionDTO.getImagenPublicacionBase64().isEmpty()) {
                        byte[] imagenPublicacionBytes = Base64.getDecoder().decode(publicacionDTO.getImagenPublicacionBase64());
                        publicacion.setImagenPublicacion(imagenPublicacionBytes);
                    }

                    publicacionRepository.save(publicacion);

                    PublicacionDTO resultDTO = Mapping.convertirAPublicacionDTO(publicacion);
                    return resultDTO;
                }
            }
        }
        return null;
    }

    @Override
    public Page<PublicacionDTO> obtenerPublicaciones(FiltroDTO filtro, int pagina, int tamano) {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = null;
        if (token != null) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("fechaPublicacion").descending());
        Page<Publicacion> pageData;

        if (filtro != null && filtro.getFechaPublicacion() != null) {
            pageData = publicacionRepository.findByFechaPublicacionAfterWithComments(filtro.getFechaPublicacion(), pageable);
        } else {
            pageData = publicacionRepository.findAllWithUserDetailsAndComments(pageable);
        }

        final List<Long> likedPublicacionIds;
        if (userId != null) {
            likedPublicacionIds = likeRepository.findByUsuario(new UsuarioEntity(userId))
                    .stream()
                    .map(like -> like.getPublicacion().getId())
                    .collect(Collectors.toList());
        } else {
            likedPublicacionIds = Collections.emptyList();
        }
        final Long finalUserId = userId;
        return pageData.map(publicacion -> {
            PublicacionDTO dto = convertToPublicacionDTO(publicacion);
            if (dto != null && likedPublicacionIds != null) {
                dto.setLiked(likedPublicacionIds.contains(publicacion.getId()));
            }

            if (finalUserId != null && dto != null && dto.getUsuario() != null) {
                boolean sigueAutor = usuarioService.obtenerUsuariosQueSigo().stream()
                        .anyMatch(seguidor -> seguidor.getId().equals(dto.getUsuario().getId()));
                dto.setSigueAutor(sigueAutor);
            }

            return dto;
        });
    }

    @Override
    @Transactional  // Asegura que la operación sea transaccional
    public ComentarioDTO comentarPublicacion(Long publicacionId, ComentarioDTO comentarioDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        UsuarioEntity usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setTexto(comentarioDTO.getTexto());
        comentario.setPublicacion(publicacion);
        comentario.setUsuario(usuario);
        Comentario savedComentario = comentarioRepository.save(comentario);

        String nombreCompleto = usuario.getDatosPersonales().getNombre() + " " + usuario.getDatosPersonales().getApellidos();
        ComentarioDTO savedComentarioDTO = new ComentarioDTO();
        savedComentarioDTO.setId(savedComentario.getId());
        savedComentarioDTO.setTexto(savedComentario.getTexto());
        savedComentarioDTO.setNombreCompleto(nombreCompleto);
        byte[] imagenPerfilBytes = usuario.getImagenPerfil();
        String imagenPerfilBase64 = Base64.getEncoder().encodeToString(imagenPerfilBytes);
        savedComentarioDTO.setImagenPerfilBase64(imagenPerfilBase64);
        ZoneId zoneId = ZoneId.of("Europe/Madrid");
        LocalDateTime fechaHoraMadrid = LocalDateTime.now(zoneId);
        String tiempoDesdeCreacion = Mapping.calculateTimeSince(savedComentario.getFechaCreacion());
        savedComentarioDTO.setFechaCreacion(tiempoDesdeCreacion);

        return savedComentarioDTO;
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorPublicacionId(Long publicacionId) {
        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        return comentarios.stream()
                .map(comentario -> {
                    UsuarioEntity usuario = comentario.getUsuario();
                    String nombreCompleto = usuario.getDatosPersonales().getNombre() + " " + usuario.getDatosPersonales().getApellidos();
                    String tiempoDesdeCreacion = Mapping.calculateTimeSince(comentario.getFechaCreacion());
                    byte[] imagenPerfilBytes = usuario.getImagenPerfil();
                    String imagenPerfilBase64 = Base64.getEncoder().encodeToString(imagenPerfilBytes); // Asegúrate de que este getter exista
                    String titular = usuario.getTitular(); // Asegúrate de que este getter exista
                    return new ComentarioDTO(
                            comentario.getPublicacion().getId(),
                            comentario.getTexto(),
                            nombreCompleto,
                            tiempoDesdeCreacion,
                            imagenPerfilBase64,
                            titular,
                            comentario.getUsuario().getId()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComentarioDTO responderAComentario(Long comentarioPadreId, ComentarioDTO comentarioDTO) {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = jwtTokenUtil.getUserIdFromToken(token);

        Comentario comentarioPadre = comentarioRepository.findById(comentarioPadreId)
                .orElseThrow(() -> new RuntimeException("Comentario padre no encontrado"));
        UsuarioEntity usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario respuesta = new Comentario();
        respuesta.setTexto(comentarioDTO.getTexto());
        respuesta.setPublicacion(comentarioPadre.getPublicacion()); // La respuesta pertenece a la misma publicación que el comentario padre
        respuesta.setUsuario(usuario);
        respuesta.setComentarioPadre(comentarioPadre); // Estableciendo el comentario padre

        Comentario savedRespuesta = comentarioRepository.save(respuesta);

        // Convierte la entidad Comentario guardada en DTO, similar al método anterior
        ComentarioDTO savedRespuestaDTO = new ComentarioDTO();
        savedRespuestaDTO.setId(savedRespuesta.getId());
        savedRespuestaDTO.setTexto(savedRespuesta.getTexto());
        savedRespuestaDTO.setNombreCompleto(usuario.getDatosPersonales().getNombre() + " " + usuario.getDatosPersonales().getApellidos());
        savedRespuestaDTO.setImagenPerfilBase64(Base64.getEncoder().encodeToString(usuario.getImagenPerfil()));
        savedRespuestaDTO.setFechaCreacion(Mapping.calculateTimeSince(savedRespuesta.getFechaCreacion()));
        savedRespuestaDTO.setComentarioPadreId(comentarioPadreId);

        return savedRespuestaDTO;
    }

    private PublicacionDTO convertToPublicacionDTO(Publicacion publicacion) {
        String imagenPerfilBase64 = Base64.getEncoder().encodeToString(publicacion.getUser().getImagenPerfil());
        String fechaPublicacion = Mapping.calculateTimeSince(publicacion.getFechaPublicacion());
        String imagenPublicacionBase64 = null;
        String giftBase64 = null;
        if (publicacion.getImagenPublicacion() != null) {
            imagenPublicacionBase64 = Base64.getEncoder().encodeToString(publicacion.getImagenPublicacion());
        }


        List<ComentarioDTO> comentarioDTOs = publicacion.getComentarios().stream()
                .map(this::convertToComentarioDTO)
                .collect(Collectors.toList());
        UsuarioDTO usuarioDTO = Mapping.toUsuarioDTO(publicacion.getUser());
        return new PublicacionDTO(
                publicacion.getId(),
                publicacion.getTitulo(),
                publicacion.getTexto(),
                fechaPublicacion,
                imagenPerfilBase64,
                publicacion.getUser().getDatosPersonales().getNombre(),
                publicacion.getUser().getDatosPersonales().getApellidos(),
                publicacion.getUser().getTitular(),
                false,  // Este valor se ajusta luego basado en 'liked'
                publicacion.getLikeCount(),
                comentarioDTOs,
                imagenPublicacionBase64,
                publicacion.getUser().getId(),
                usuarioDTO




        );
    }


    private ComentarioDTO convertToComentarioDTO(Comentario comentario) {
        String imagenPerfilBase64 = Base64.getEncoder().encodeToString(comentario.getUsuario().getImagenPerfil());
        String nombreCompleto = comentario.getUsuario().getDatosPersonales().getNombre() + " " + comentario.getUsuario().getDatosPersonales().getApellidos();
        String fechaCreacion = Mapping.calculateTimeSince(comentario.getFechaCreacion());
        String titular = comentario.getUsuario().getTitular();

        ComentarioDTO comentarioDTO = new ComentarioDTO(
                comentario.getId(),
                comentario.getTexto(),
                nombreCompleto,
                fechaCreacion,
                imagenPerfilBase64,
                titular,
                comentario.getUsuario().getId()

        );

        if (comentario.getRespuestas() != null && !comentario.getRespuestas().isEmpty()) {
            List<ComentarioDTO> respuestasDTO = comentario.getRespuestas().stream()
                    .map(this::convertToComentarioDTO)  // Recursivamente convertir respuestas
                    .collect(Collectors.toList());
            comentarioDTO.setRespuestas(respuestasDTO);
        }
        return comentarioDTO;
    }





}
