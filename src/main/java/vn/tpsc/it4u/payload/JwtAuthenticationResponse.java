package vn.tpsc.it4u.payload;

import lombok.Data;

/**
 * JwtAuthenticationResponse
 */
@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}