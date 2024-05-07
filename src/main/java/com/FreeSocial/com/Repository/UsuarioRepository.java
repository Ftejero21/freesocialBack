package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.UsuarioEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Long> {

    public UsuarioEntity findByEmail(String email);

     @Query("SELECT u FROM UsuarioEntity u WHERE u.fechaExpiracionCodigoRecuperacion < :fechaActual AND u.codigoRecuperacion IS NOT NULL")
     public List<UsuarioEntity> findUsuariosConCodigosExpirados(@Param("fechaActual") Date fechaActual);


    @Query("SELECT u FROM UsuarioEntity u JOIN u.datosPersonales dp WHERE dp.nombre LIKE CONCAT(:nombre, '%') AND u.id <> :usuarioLogeadoId")
    public List<UsuarioEntity> findByNombreUsuario(@Param("nombre") String nombre,@Param("usuarioLogeadoId") Long usuarioLogeadoId);

    public Optional<UsuarioEntity> findById(Long id);
}
