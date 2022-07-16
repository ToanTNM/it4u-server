package vn.tpsc.it4u.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

	@Bean
	Key key() {
		Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		return key;
	}

	public String generateToken(Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
				.setSubject(Long.toString(customUserDetails.getId()))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(key(), SignatureAlgorithm.HS512)
				.compact();
	}

	/**
	 * Generates a token from a principal object. Embed the refresh token in the jwt
	 * so that a new jwt can be created
	 */
	public String generateTokenFromUserId(Long userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		return Jwts.builder()
				.setSubject(Long.toString(userId))
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(key(), SignatureAlgorithm.HS512)
				.compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();

		return Long.parseLong(claims.getSubject());

		// Claims claims = Jwts.parser()
		// .setSigningKey(jwtSecret)
		// .parseClaimsJws(token)
		// .getBody();

		// // return claims.getSubject();
		// return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build()
					.parseClaimsJws(authToken);
			// Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

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