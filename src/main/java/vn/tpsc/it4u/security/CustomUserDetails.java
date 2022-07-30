package vn.tpsc.it4u.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.tpsc.it4u.enums.Gender;
import vn.tpsc.it4u.enums.UserStatus;
import vn.tpsc.it4u.enums.UserType;
import vn.tpsc.it4u.models.SitesName;
import vn.tpsc.it4u.models.auth.Role;
import vn.tpsc.it4u.models.auth.User;

/**
 * CustomUserDetails
 * Spring Security will use the information stored in the this class' object to
 * perform authentication and authorization
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

	@Id
	private Long id;

	private String name;

	private String username;

	@JsonIgnore
	private String password;

	@JsonIgnore
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

	private Collection<? extends GrantedAuthority> authorities;

	public static CustomUserDetails create(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		return new CustomUserDetails(
				user.getId(),
				user.getName(),
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.getAvatar(),
				user.getGender(),
				user.getType(),
				user.getStatus(),
				user.getSitename(),
				user.getLastTimeLogin(),
				user.getNumLogin(),
				user.getLanguage(),
				user.getRoles(),
				user.getRegistrationId(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}