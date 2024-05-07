package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario,Long> {

    List<Comentario> findByPublicacionId(Long publicacionId);
}
