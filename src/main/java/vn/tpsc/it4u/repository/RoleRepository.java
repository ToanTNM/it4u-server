package vn.tpsc.it4u.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.enums.RoleName;
import vn.tpsc.it4u.models.auth.Role;

/**
 * RoleRepository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);

	@Query(value = " SELECT"
			+ " *"
			+ " FROM roles"
			+ " WHERE name=:name", nativeQuery = true)
	Role findRoleByName(@Param("name") String name);
}