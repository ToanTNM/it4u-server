package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.SystemConfig;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

}
