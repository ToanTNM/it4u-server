package vn.tpsc.it4u.payloads.auth;

import lombok.Data;

/**
 * JwtAuthenticationResponse
 */
@Data
public class JwtAuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";

	public JwtAuthenticationResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}