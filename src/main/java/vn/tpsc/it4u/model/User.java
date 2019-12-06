package vn.tpsc.it4u.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.model.audit.UserDateAudit;
import vn.tpsc.it4u.model.enums.Gender;
import vn.tpsc.it4u.model.enums.UserStatus;
import vn.tpsc.it4u.model.enums.UserType;

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
    @Size(max = 15)
    private String username;

    @NotEmpty
    @Size(max = 40)
    @Email
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String password;

    @Size(max = 500)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    // @ColumnDefault("Client")
    private UserType type;

    @Enumerated(EnumType.STRING)
    // @ColumnDefault("Active")
    private UserStatus status;

    //@DBRef(lazy = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password, Gender gender, UserType type, UserStatus status) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.type = type;
        this.status = status;
    }
}