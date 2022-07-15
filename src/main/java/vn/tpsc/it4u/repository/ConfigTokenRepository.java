package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.ConfigToken;

/**
 * ConfigTokenRepository
 */
@Repository
public interface ConfigTokenRepository extends JpaRepository<ConfigToken, Long> {
    List<ConfigToken> findAll();
}