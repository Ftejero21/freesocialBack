package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.EducacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducacionRepository extends JpaRepository<EducacionEntity,Long> {

    public List<EducacionEntity> findByUsuarioIdOrderByFechaFinDescFechaInicioAsc(Long usuarioId);

    public Optional<EducacionEntity> findByIdAndUsuarioId(Long id, Long usuarioId);

}
