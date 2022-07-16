package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tpsc.it4u.models.audit.UserDateAudit;

/**
 * ChannelAttribute
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChannelAttribute extends UserDateAudit {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 40)
	private String customer;

	@OneToOne
	@JoinColumn(name = "channel_value_id")
	private ChannelValue channelValue;

	@NotEmpty
	@Size(max = 40)
	private String status;

	@Size(max = 40)
	private String virtualNum;

	@Size(max = 40)
	private String usernamePPPoE;

	public ChannelAttribute(String customer, String status, String virtualNum, String usernamePPPoE,
			ChannelValue channelValue) {
		this.customer = customer;
		this.status = status;
		this.virtualNum = virtualNum;
		this.usernamePPPoE = usernamePPPoE;
		this.channelValue = channelValue;
	}
}
