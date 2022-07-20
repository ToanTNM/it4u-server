package vn.tpsc.it4u.payloads;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import vn.tpsc.it4u.models.enums.Gender;
import vn.tpsc.it4u.models.enums.UserStatus;
import vn.tpsc.it4u.models.enums.UserType;

/**
 * SignUpRequest
 */
@Data
public class SignUpRequest {

	@NotEmpty
	@Size(min = 4, max = 40)
	private String name;

	@NotEmpty
	@Size(min = 3, max = 100)
	private String username;

	@NotEmpty
	@Size(max = 40)
	@Email
	private String email;

	@NotEmpty
	@Size(min = 6, max = 20)
	private String password;

	private Gender gender;

	@NotEmpty
	private UserType type;

	private UserStatus status;

	private Set<String> sitename;

	private String language;

	@NotEmpty
	private String roles;
}