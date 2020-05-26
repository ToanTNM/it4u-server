package vn.tpsc.it4u.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.Reporter;;

/**
 * ReporterRepository
 */
@Repository
public interface ReporterRepository extends JpaRepository<Reporter, Long> {
    List<Reporter> findAll();

    Optional<Reporter> findBySitename(String sitename);

    List<Reporter> findByGroupClient(String service);

    List<Reporter> findByIdIn(List<Long> sitenameIds);

    // List<Reporter> findByList(String userIds);
    
    Boolean existsBySitename(String sitename);

}