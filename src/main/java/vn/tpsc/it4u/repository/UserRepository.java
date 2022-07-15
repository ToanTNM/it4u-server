package vn.tpsc.it4u.repository;

import java.util.List;
import java.util.Optional;

// import javax.jws.soap.SOAPBinding.Use;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.User;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);

	Optional<User> findByRefreshToken(String refreshToken);

	List<User> findByIdIn(List<Long> userIds);

	User findById(long id);

	User findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByResetToken(String resetToken);

	@Query(value = " Select *"
			+ " FROM users u"
			+ " INNER JOIN user_roles ON user_roles.user_id=u.id"
			+ " INNER JOIN roles ON user_roles.role_id=roles.id"
			+ " INNER JOIN user_site ON user_site.user_id=u.id"
			+ " INNER JOIN sitesname ON user_site.site_id=sitesname.id"
			+ " WHERE u.name like %:param%", nativeQuery = true)
	List<User> findAllUserByParam(@Param(value = "param") String param);
}