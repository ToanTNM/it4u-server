package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.HandleService;

@Repository
public interface HandleServiceRepository extends JpaRepository<HandleService, Long> {

}
