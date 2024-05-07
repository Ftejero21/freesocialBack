package com.FreeSocial.com.Controller.PublicacionController;


import com.FreeSocial.com.Service.PublicacionService.PublicacionService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.V.O.DTO.ComentarioDTO;
import com.FreeSocial.com.V.O.DTO.FiltroDTO;
import com.FreeSocial.com.V.O.DTO.PublicacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = FreeSocialConstants.PUBLICACION_URL)
@CrossOrigin(origins = "*")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;


    @PostMapping(value = FreeSocialConstants.SAVEORUPDATE_PUBLICACION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> saveOrUpdatePublicacion(@RequestBody  PublicacionDTO publicacionDTO) {
        PublicacionDTO proyectoGuardado = publicacionService.saveOrUpdatePublicacion(publicacionDTO);
        if (proyectoGuardado != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("publicacion", proyectoGuardado);

            String message = (publicacionDTO.getId() != null) ? "Publicación actualizada con éxito" : "Publicación guardada con éxito";
            responseMap.put("message", message);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = FreeSocialConstants.GET_PUBLICACIONES)
    public ResponseEntity<Page<PublicacionDTO>> obtenerPublicaciones(FiltroDTO filtro,@RequestParam(defaultValue = "0") int pagina,@RequestParam(defaultValue = "10") int tamano) {
        Page<PublicacionDTO> publicacionesDTO = publicacionService.obtenerPublicaciones(filtro, pagina, tamano);
        return ResponseEntity.ok(publicacionesDTO);
    }

    @PostMapping(value = FreeSocialConstants.CREATE_COMMENT +"/{publicacionId}")
    public ResponseEntity<ComentarioDTO> agregarComentario(@PathVariable Long publicacionId, @RequestBody ComentarioDTO comentarioDTO) {
        ComentarioDTO nuevoComentario = publicacionService.comentarPublicacion(publicacionId, comentarioDTO);
        return ResponseEntity.ok(nuevoComentario);
    }

    @GetMapping(value = FreeSocialConstants.GET_COMMENTS + "/{publicacionId}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentarios(@PathVariable Long publicacionId) {
        List<ComentarioDTO> comentarios = publicacionService.obtenerComentariosPorPublicacionId(publicacionId);
        return ResponseEntity.ok(comentarios);
    }

    @PostMapping("/comentarios/{comentarioPadreId}/respuesta")
    public ResponseEntity<ComentarioDTO> responderAComentario(@PathVariable Long comentarioPadreId, @RequestBody ComentarioDTO comentarioDTO) {
        ComentarioDTO respuestaDTO = publicacionService.responderAComentario(comentarioPadreId, comentarioDTO);
        return ResponseEntity.ok(respuestaDTO);
    }
}
