package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.ServiceDeployment;

@Repository
public interface ServiceDeploymentRepository extends JpaRepository<ServiceDeployment, Long> {
    
}
