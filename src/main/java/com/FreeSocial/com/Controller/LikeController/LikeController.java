package com.FreeSocial.com.Controller.LikeController;

import com.FreeSocial.com.Service.Like.LikeService;
import com.FreeSocial.com.Service.PublicacionService.PublicacionService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = FreeSocialConstants.Like_URL)
@CrossOrigin(origins = "*")
public class LikeController {



    @Autowired
    private LikeService likeService;

    // Endpoint para dar like a una publicación
    @PostMapping(value = FreeSocialConstants.DAR_LIKE + "/{publicacionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> darLikeALaPublicacion(@PathVariable Long publicacionId) {

            likeService.darLikeALaPublicacion(publicacionId);
            return ResponseEntity.ok().body("{\"message\":\"Like añadido con éxito\"}");

    }

    // Endpoint para verificar si un usuario ha dado like a una publicación
    @GetMapping( value = FreeSocialConstants.GET_PUBLICACIONES_LIKE + "/{publicacionId}")
    public ResponseEntity<Boolean> usuarioHaDadoLike(@PathVariable Long publicacionId) {
        boolean hasLiked = likeService.usuarioHaDadoLike(publicacionId);
        return ResponseEntity.ok(hasLiked);
    }
}
