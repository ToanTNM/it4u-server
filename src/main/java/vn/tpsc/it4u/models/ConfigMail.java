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
 * ConfigMail
 */
@Getter
@Setter
@NoArgsConstructor
// @Document("configmail")
@Entity
@Table(name = "configMail")
public class ConfigMail extends UserDateAudit {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 40)
	private String serviceMail;

	@NotEmpty
	@Size(max = 40)
	private String hostMail;

	@NotEmpty
	@Size(max = 40)
	private String portMail;

	@NotEmpty
	@Size(max = 40)
	private String maxMessages;

	@NotEmpty
	@Size(max = 40)
	private String rateDelta;

	@NotEmpty
	@Size(max = 40)
	private String rateLimit;

	@NotEmpty
	@Size(max = 40)
	private String usernameMail;

	@NotEmpty
	@Size(max = 40)
	private String passwordMail;

	@NotEmpty
	@Size(max = 255)
	private String ccMail;

	@NotEmpty
	@Size(max = 255)
	private String subjectMail;

	@NotEmpty
	@Size(max = 255)
	private String textMail;

	@NotEmpty
	@Size(max = 255)
	private String cronjobMail;

	public ConfigMail(String serviceMail, String hostMail, String portMail, String maxMessages, String rateDelta,
			String rateLimit, String usernameMail, String passwordMail, String ccMail, String subjectMail,
			String textMail, String cronjobMail) {
		this.serviceMail = serviceMail;
		this.hostMail = hostMail;
		this.portMail = portMail;
		this.maxMessages = maxMessages;
		this.rateDelta = rateDelta;
		this.rateLimit = rateLimit;
		this.usernameMail = usernameMail;
		this.passwordMail = passwordMail;
		this.ccMail = ccMail;
		this.subjectMail = subjectMail;
		this.textMail = textMail;
		this.cronjobMail = cronjobMail;
	}
}