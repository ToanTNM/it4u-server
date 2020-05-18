package vn.tpsc.it4u.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.model.SitesName;
import vn.tpsc.it4u.model.enums.Gender;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.enums.UserType;

/**
 * CustomUserDetails
 * Spring Security will use the information stored in the this class' object to perform authentication and authorization
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

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

    private String language;

    private Set<Role> roles;

    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

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
                user.getLanguage(),
                user.getRoles(),
                authorities
        );
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