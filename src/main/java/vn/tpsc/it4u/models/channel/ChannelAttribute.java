package vn.tpsc.it4u.models.channel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.tpsc.it4u.models.audit.UserDateAudit;

/**
 * ChannelAttribute
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
public class ChannelAttribute extends UserDateAudit {

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

	// public ChannelAttribute(String customer, String status, String virtualNum,
	// String usernamePPPoE,
	// ChannelValue channelValue) {
	// this.customer = customer;
	// this.status = status;
	// this.virtualNum = virtualNum;
	// this.usernamePPPoE = usernamePPPoE;
	// this.channelValue = channelValue;
	// }
}
