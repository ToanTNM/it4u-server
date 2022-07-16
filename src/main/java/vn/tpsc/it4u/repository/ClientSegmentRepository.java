package vn.tpsc.it4u.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ClientSegment;

/**
 * ClientSegmentRepository
 */

@Repository
public interface ClientSegmentRepository extends JpaRepository<ClientSegment, Long> {

	Optional<ClientSegment> findByName(String name);

	Boolean existsByName(String name);
}
