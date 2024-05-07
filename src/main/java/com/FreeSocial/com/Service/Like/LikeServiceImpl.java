package com.FreeSocial.com.Service.Like;

import com.FreeSocial.com.Config.JwtTokenUtil;
import com.FreeSocial.com.Repository.LikeRepository;
import com.FreeSocial.com.Repository.PublicacionRepository;
import com.FreeSocial.com.V.O.Entity.Likes;
import com.FreeSocial.com.V.O.Entity.Publicacion;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Override
    public void darLikeALaPublicacion(Long publicacionId) {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = null;
        if (token != null) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }

        Publicacion publicacion = publicacionRepository.findById(publicacionId).orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        UsuarioEntity usuario = new UsuarioEntity(userId);
        Publicacion publicacionEntity = new Publicacion(publicacionId);

        Likes existingLike = likeRepository.findByUsuarioAndPublicacion(usuario, publicacionEntity);
        if (existingLike != null) {
            // Si ya existe un like, lo eliminamos
            likeRepository.delete(existingLike);
            publicacion.setLikeCount(publicacion.getLikeCount() - 1);  // Decrementar el contador de likes
        } else {
            // Si no existe, añadimos un nuevo like
            Likes like = new Likes();
            like.setUsuario(usuario);
            like.setPublicacion(publicacionEntity);
            like.setFechaLike(LocalDateTime.now());
            likeRepository.save(like);
            publicacion.setLikeCount(publicacion.getLikeCount() + 1);  // Incrementar el contador de likes
        }
        publicacionRepository.save(publicacion);
    }

    @Override
    public boolean usuarioHaDadoLike(Long publicacionId) {
        String token = jwtTokenUtil.getCurrentToken();
        Long userId = null;
        if (token != null) {
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }
        return likeRepository.existsByUsuarioAndPublicacion(new UsuarioEntity(userId), new Publicacion(publicacionId));

    }
}
