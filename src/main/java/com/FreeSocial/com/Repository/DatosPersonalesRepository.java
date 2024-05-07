package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.DatosPersonalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatosPersonalesRepository extends JpaRepository<DatosPersonalesEntity,Long> {
}
