package vn.tpsc.it4u.payloads;

import lombok.Data;

/**
 * ChannelAttributeRequest
 */
@Data
public class ChannelAttributeRequest {

	private String customer;

	private String channelValue;

	private String status;

	private String virtualNum;

	private String usernamePPPoE;
}
