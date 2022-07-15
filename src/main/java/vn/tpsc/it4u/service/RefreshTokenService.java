package vn.tpsc.it4u.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.token.RefreshToken;
import vn.tpsc.it4u.util.Util; 
import vn.tpsc.it4u.repository.RefreshTokenRepository;
import vn.tpsc.it4u.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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