package vn.tpsc.it4u.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.tpsc.it4u.models.token.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	@Override
	Optional<RefreshToken> findById(Long id);

	Optional<RefreshToken> findByToken(String token);
}