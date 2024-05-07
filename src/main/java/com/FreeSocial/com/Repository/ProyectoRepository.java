package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.ExperienciaLaboralEntity;
import com.FreeSocial.com.V.O.Entity.ProyectosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<ProyectosEntity , Long> {

    @Query("SELECT e FROM ProyectosEntity e WHERE e.usuario.id = :userId ORDER BY CASE WHEN e.fechaFin IS NULL THEN '1' ELSE '2' END ASC, e.fechaFin DESC, e.fechaInicio ASC")
    public List<ProyectosEntity> findByUsuarioIdOrderByFechaFinAscFechaInicioAsc(Long userId);
}
