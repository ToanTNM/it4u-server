package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.models.audit.UserDateAudit;

/**
 * ConfigToken
 */
@Getter
@Setter
@NoArgsConstructor
// @Document("configtoken")
@Entity
@Table(name = "configtoken", uniqueConstraints = { @UniqueConstraint(columnNames = { "csrfToken" }),
		@UniqueConstraint(columnNames = { "unifises" }) })
public class ConfigToken extends UserDateAudit {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 40)
	private String csrfToken;

	@NotEmpty
	@Size(max = 40)
	private String unifises;

	public ConfigToken(String csrfToken, String unifises) {
		this.csrfToken = csrfToken;
		this.unifises = unifises;
	}
}