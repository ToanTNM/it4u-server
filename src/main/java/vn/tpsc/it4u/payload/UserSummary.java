package vn.tpsc.it4u.payload;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.model.enums.Gender;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.enums.UserType;

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
    private String sitename;
    private String language;
    private Set<Role> roles;
}
