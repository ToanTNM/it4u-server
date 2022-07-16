package vn.tpsc.it4u.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.models.audit.UserDateAudit;
import lombok.NoArgsConstructor;

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
