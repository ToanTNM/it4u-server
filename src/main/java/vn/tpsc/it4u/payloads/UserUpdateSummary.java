package vn.tpsc.it4u.payloads;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.models.Role;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.enums.Gender;
import vn.tpsc.it4u.models.enums.UserStatus;
import vn.tpsc.it4u.models.enums.UserType;

@AllArgsConstructor
@Getter
@Setter
public class UserUpdateSummary {
	private Long id;
	private String username;
	private String name;
	private String email;
	private String avatar;
	private Gender gender;
	private String password;
	private UserType type;
	private UserStatus status;
	private Set<SitesName> sitename;
	private Long lastTimeLogin;
	private Long numLogin;
	private String language;
	private Set<Role> roles;
	private String registrationId;
}
