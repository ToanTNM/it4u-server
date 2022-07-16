package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ConfigMail;

/**
 * ConfigMailRepository
 */
@Repository
public interface ConfigMailRepository extends JpaRepository<ConfigMail, Long> {
	List<ConfigMail> findAll();
}