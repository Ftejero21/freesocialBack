package com.FreeSocial.com.Repository;


import com.FreeSocial.com.V.O.Entity.DatosPersonalesEntity;
import com.FreeSocial.com.V.O.Entity.EducacionEntity;
import com.FreeSocial.com.V.O.Entity.ExperienciaLaboralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienciaLaboralRepositorio extends JpaRepository<ExperienciaLaboralEntity,Long> {


    @Query("SELECT e FROM ExperienciaLaboralEntity e WHERE e.usuario.id = :userId ORDER BY CASE WHEN e.fechaFin IS NULL THEN '1' ELSE '2' END ASC, e.fechaFin DESC, e.fechaInicio ASC")
    public List<ExperienciaLaboralEntity> findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(Long userId);
    public Optional<ExperienciaLaboralEntity> findByIdAndUsuarioId(Long id, Long usuarioId);
}
