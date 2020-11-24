package vn.tpsc.it4u.payload;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.model.ChannelName;
import vn.tpsc.it4u.model.ChannelValue;

@AllArgsConstructor
@Getter
@Setter
public class ChannelAttributeSummary {

    private Long id;

    private String customer;

    private ChannelValue channelValue;

    private String status;

    private String virtualNum;

    private String usernamePPPoE;
}
