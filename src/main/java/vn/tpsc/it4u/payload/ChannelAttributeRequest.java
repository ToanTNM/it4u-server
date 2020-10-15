package vn.tpsc.it4u.payload;

import java.util.Set;

import lombok.Data;

/**
 * ChannelAttributeRequest
 */
@Data
public class ChannelAttributeRequest {
    private String channelName;
    private String channelValue;
    private String status;
}
