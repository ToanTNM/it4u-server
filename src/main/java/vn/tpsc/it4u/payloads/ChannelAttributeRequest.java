package vn.tpsc.it4u.payloads;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * ChannelAttributeRequest
 */
@Data
public class ChannelAttributeRequest {

	@NotEmpty
	private String customer;

	@NotEmpty
	private String channelValue;

	@NotEmpty
	private String status;

	@NotEmpty
	private String virtualNum;

	@NotEmpty
	private String usernamePPPoE;
}
