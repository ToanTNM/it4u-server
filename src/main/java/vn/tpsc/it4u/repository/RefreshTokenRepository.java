package vn.tpsc.it4u.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.auth.RefreshToken;
import vn.tpsc.it4u.models.auth.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findById(Long id);

	Optional<RefreshToken> findByToken(String token);

	@Modifying
	int deleteByUser(User user);
}