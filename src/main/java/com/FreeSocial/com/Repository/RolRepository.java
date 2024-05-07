package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<RolEntity,Long> {
}
