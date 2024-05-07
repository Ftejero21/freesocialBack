package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.CiudadesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends JpaRepository<CiudadesEntity,Long> {
}
