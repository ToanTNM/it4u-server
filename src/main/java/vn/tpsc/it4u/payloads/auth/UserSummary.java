package vn.tpsc.it4u.payloads.auth;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.enums.Gender;
import vn.tpsc.it4u.enums.UserStatus;
import vn.tpsc.it4u.enums.UserType;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.auth.Role;

@AllArgsConstructor
@Getter
@Setter
public class UserSummary {
	private Long id;
	private String username;
	private String name;
	private String email;
	private String avatar;
	private Gender gender;
	private UserType type;
	private UserStatus status;
	private Set<SitesName> sitename;
	private Long lastTimeLogin;
	private Long numLogin;
	private String language;
	private Set<Role> roles;
	private String registrationId;
}
