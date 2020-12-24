package vn.tpsc.it4u.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;
import vn.tpsc.it4u.model.enums.Gender;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.enums.UserType;
import vn.tpsc.it4u.model.token.RefreshToken;

/**
 * User
 */
@Getter
@Setter
@NoArgsConstructor
//@Document("user")
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
public class User extends UserDateAudit {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    private String name;

    @NotEmpty
    @Size(max = 40)
    private String username;

    @NotEmpty
    @Size(max = 40)
    @Email
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String password;

    @NotEmpty
    @Size(max = 100)
    private String language;

    @Size(max = 500)
    private String avatar;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    //@ColumnDefault("Client")
    private UserType type;

    @Enumerated(EnumType.STRING)
    //@ColumnDefault("Active")
    private UserStatus status;

    // @Size(max = 100)
    // private String sitename;

    @Size(max = 100)
    private String resetToken;

    //@DBRef(lazy = true)
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany @Fetch(FetchMode.JOIN)
    // @JsonIgnore
    @JoinTable(name = "user_site",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id"))

    private Set<SitesName> sitename = new HashSet<>();

    private Long lastTimeLogin;

    private Long numLogin;

    private String refreshToken;

    private String registrationId;

    public User(long id) {
        this.id = id;
    }

    public User(String name, String username, String email, String password, Gender gender, UserType type, UserStatus status, String language, String resetToken,
            String refreshToken, String registrationId) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.type = type;
        this.status = status;
        // this.sitename = sitename;
        this.language = language;
        this.resetToken = resetToken;
        this.refreshToken = refreshToken;
        this.registrationId = registrationId;
    }
}