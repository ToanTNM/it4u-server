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
    private Set<ChannelName> channelName;
    private Set<ChannelValue> channelValue;
    private String status;
}
