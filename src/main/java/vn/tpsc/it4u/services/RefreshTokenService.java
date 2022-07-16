package vn.tpsc.it4u.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.token.RefreshToken;
import vn.tpsc.it4u.repository.RefreshTokenRepository;
import vn.tpsc.it4u.utils.StringUtils;
import vn.tpsc.it4u.utils.Util;

@Service
@ExtensionMethod({ StringUtils.class })
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	/**
	 * Creates and returns a new refresh token
	 */
	@Value("${app.jwtExpirationInMs}")
	private Long jwtExpirationInMs;

	public String createRefreshToken() {
		String refreshToken = Util.generateRandomUuid();
		return refreshToken;
	}

	/**
	 * Persist the updated refreshToken instance to database
	 */
	public RefreshToken save(RefreshToken refreshToken) {
		return refreshTokenRepository.save(refreshToken);
	}
}