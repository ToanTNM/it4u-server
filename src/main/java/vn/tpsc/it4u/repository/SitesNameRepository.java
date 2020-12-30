package vn.tpsc.it4u.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.SitesName;

/**
 * SitesNameRepository
 */
@Repository
public interface SitesNameRepository extends JpaRepository<SitesName, Long> {
    List<SitesName> findAll();
    
    Boolean existsBySitename(String username);
    
    Boolean existsByIdname(String idname);

    SitesName findByIdname(String idname);

    SitesName findBySitename(String sitename);
}