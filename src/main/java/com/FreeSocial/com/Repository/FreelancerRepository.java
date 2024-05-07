package com.FreeSocial.com.Repository;

import com.FreeSocial.com.V.O.Entity.FreelancerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerRepository extends JpaRepository<FreelancerEntity,Long> {
}
