package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {

    /*@Query("SELECT p, u.imagenPerfil,u.titular ,d.nombre, d.apellidos FROM Publicacion p JOIN p.user u JOIN u.datosPersonales d WHERE p.fechaPublicacion >= :fechaInicio")
    Page<Object[]> findByFechaPublicacionAfter(Date fechaInicio, Pageable pageable);*/

    /*@Query("SELECT p, u.imagenPerfil, u.titular,d.nombre, d.apellidos FROM Publicacion p JOIN p.user u JOIN u.datosPersonales d")
    Page<Object[]> findAllWithUserDetails(Pageable pageable);*/



    @Query(
            value = "SELECT DISTINCT p FROM Publicacion p LEFT JOIN FETCH p.comentarios c JOIN FETCH p.user u JOIN FETCH u.datosPersonales WHERE p.fechaPublicacion >= :fechaInicio AND c.comentarioPadre IS NULL",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Publicacion p JOIN p.user u WHERE p.fechaPublicacion >= :fechaInicio"
    )
    Page<Publicacion> findByFechaPublicacionAfterWithComments(Date fechaInicio, Pageable pageable);


    @Query(
            value = "SELECT DISTINCT p FROM Publicacion p LEFT JOIN FETCH p.comentarios c JOIN FETCH p.user u JOIN FETCH u.datosPersonales WHERE c.comentarioPadre IS NULL",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Publicacion p JOIN p.user u"
    )
    Page<Publicacion> findAllWithUserDetailsAndComments(Pageable pageable);


}
