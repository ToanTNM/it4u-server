package vn.tpsc.it4u.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tpsc.it4u.models.Contract;
import vn.tpsc.it4u.models.channel.ChannelAttribute;

@AllArgsConstructor
@Getter
@Setter
public class ChannelDetailSummary {
	private Long id;

	private Contract contract;

	private ChannelAttribute channelAttribute;

	private String routerType;

	private String votesRequire;

	private String ipType;

	private String deviceStatus;

	private String ipAddress;

	private String regionalEngineer;

	private Long deployRequestDate;

	private Long dateAcceptance;

	private String fees;
}
