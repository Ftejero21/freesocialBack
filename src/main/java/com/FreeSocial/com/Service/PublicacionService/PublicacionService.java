package com.FreeSocial.com.Service.PublicacionService;

import com.FreeSocial.com.V.O.DTO.ComentarioDTO;
import com.FreeSocial.com.V.O.DTO.FiltroDTO;
import com.FreeSocial.com.V.O.DTO.PublicacionDTO;
import com.FreeSocial.com.V.O.Entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PublicacionService {

    public PublicacionDTO saveOrUpdatePublicacion(PublicacionDTO publicacionDTO);

    public Page<PublicacionDTO> obtenerPublicaciones(FiltroDTO filtro, int pagina, int tamano);


    public ComentarioDTO comentarPublicacion(Long publicacionId, ComentarioDTO comentarioDTO);

    public List<ComentarioDTO> obtenerComentariosPorPublicacionId(Long publicacionId);

    public ComentarioDTO responderAComentario(Long comentarioPadreId, ComentarioDTO comentarioDTO);


    }
