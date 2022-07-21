package vn.tpsc.it4u.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.tpsc.it4u.models.audit.UserDateAudit;
import vn.tpsc.it4u.models.enums.Gender;
import vn.tpsc.it4u.models.enums.UserStatus;
import vn.tpsc.it4u.models.enums.UserType;

/**
 * User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"username"
		}),
		@UniqueConstraint(columnNames = {
				"email"
		})
})
@DynamicInsert
public class User extends UserDateAudit {

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

	@Column(nullable = false, columnDefinition = "varchar(20) default 'en-US'")
	// @ColumnDefault("'en-US'")
	private String language;

	@Size(max = 500)
	private String avatar;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default 'MALE'")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private UserType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(20) default 'ACTIVE'")
	private UserStatus status;

	@Size(max = 100)
	private String resetToken;

	// @DBRef(lazy = true)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Fetch(FetchMode.JOIN)
	// @JsonIgnore
	@JoinTable(name = "user_site", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "site_id"))
	private Set<SitesName> sitename;

	private Long lastTimeLogin;

	private Long numLogin;

	private String refreshToken;

	private String registrationId;

	// public User(long id) {
	// this.id = id;
	// }

	// public User(String name, String username, String email, String password,
	// Gender gender, UserType type,
	// UserStatus status, String language, String resetToken,
	// String refreshToken, String registrationId) {
	// this.name = name;
	// this.username = username;
	// this.email = email;
	// this.password = password;
	// this.gender = gender;
	// this.type = type;
	// this.status = status;
	// // this.sitename = sitename;
	// this.language = language;
	// this.resetToken = resetToken;
	// this.refreshToken = refreshToken;
	// this.registrationId = registrationId;
	// }
}