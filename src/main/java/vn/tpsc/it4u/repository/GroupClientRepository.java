package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.GroupClient;

/**
 * GroupClientRepository
 */
@Repository
public interface GroupClientRepository extends JpaRepository<GroupClient, Long> {

	List<GroupClient> findAll();

	List<GroupClient> findByIdIn(List<Long> idGroup);

}