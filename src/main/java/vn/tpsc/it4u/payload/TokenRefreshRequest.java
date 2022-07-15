package vn.tpsc.it4u.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The jwt token refresh request payload", defaultValue = "Token refresh Request")
public class TokenRefreshRequest {

    @NotBlank(message = "Refresh token cannot be blank")
    @Schema(description = "Valid refresh token passed during earlier successful authentications", required = true, allowableValues = "NonEmpty String")
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRefreshRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
