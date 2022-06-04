package vn.tpsc.it4u.security;

import java.util.Date;

import javax.crypto.SecretKey;

// import org.slf4j.log;
// import org.slf4j.logFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtTokenProvider
 * Generating a JWT after a user logs in successfully, and validating the JWT
 * sent in the Authorization header of the requests
 */
@Component
@Slf4j
public class JwtTokenProvider {
	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	// SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateToken(Authentication authentication) {

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
				.setSubject(Long.toString(customUserDetails.getId()))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(secretKey, SignatureAlgorithm.HS512)
				.compact();
	}

	public Long getUserIdFromJWT(String token) {
		// Claims claims = Jwts.parse(token)
		// .setSigningKey(secretKey)
		// .parseClaimsJws(token)
		// .getBody();
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			// Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			Jwts.parserBuilder().setSigningKey(secretKey).build()
					.parseClaimsJws(authToken);
			return true;
		} catch (InvalidClaimException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}
}