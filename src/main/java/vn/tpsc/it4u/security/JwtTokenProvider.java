package vn.tpsc.it4u.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
				.setSubject(customUserDetails.getId().toString())
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
				.setSubject(userId.toString())
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
		} catch (JwtException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("Another error ", ex.getMessage());
		}
		return false;
	}
}