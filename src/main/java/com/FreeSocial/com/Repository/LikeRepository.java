package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.Likes;
import com.FreeSocial.com.V.O.Entity.Publicacion;
import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    boolean existsByUsuarioAndPublicacion(UsuarioEntity usuario, Publicacion publicacion);
    List<Likes> findByUsuario(UsuarioEntity usuario);

    Likes findByUsuarioAndPublicacion(UsuarioEntity usuario, Publicacion publicacion);



}