package vn.tpsc.it4u.payloads.auth;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * LoginRequest
 */
@Data
@AllArgsConstructor
public class LoginRequest {

	@NotEmpty
	private String usernameOrEmail;

	@NotEmpty
	private String password;
}