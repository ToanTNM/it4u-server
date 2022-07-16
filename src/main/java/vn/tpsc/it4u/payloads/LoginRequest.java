package vn.tpsc.it4u.payloads;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * LoginRequest
 */
@Data
@AllArgsConstructor
public class LoginRequest {

	@NotBlank
	private String usernameOrEmail;

	@NotBlank
	private String password;
}