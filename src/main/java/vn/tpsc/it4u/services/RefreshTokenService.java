package vn.tpsc.it4u.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.exceptions.TokenRefreshException;
import vn.tpsc.it4u.models.auth.RefreshToken;
import vn.tpsc.it4u.repository.RefreshTokenRepository;
import vn.tpsc.it4u.repository.UserRepository;
import vn.tpsc.it4u.utils.StringUtils;

@Service
@ExtensionMethod({ StringUtils.class })
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Value("${app.refreshTokenExpirationInMs}")
	private Long refreshTokenExpirationInMs;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = RefreshToken.builder()
				.user(userRepository.findById(userId).get())
				.expiryDate(Instant.now().plusMillis(refreshTokenExpirationInMs))
				.token(UUID.randomUUID().toString())
				.build();

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	/**
	 * Persist the updated refreshToken instance to database
	 */
	public RefreshToken save(RefreshToken refreshToken) {
		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}