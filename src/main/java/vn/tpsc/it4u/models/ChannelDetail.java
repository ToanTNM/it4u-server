package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channelDetail")
public class ChannelDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "contract_id")
	private Contract contract;

	private String routerType;

	@Size(max = 40)
	private String votesRequire;

	@Size(max = 40)
	private String ipType;

	@Size(max = 40)
	private String deviceStatus;

	@Size(max = 40)
	private String ipAddress;

	@Size(max = 40)
	private String regionalEngineer;

	private Long deployRequestDate;

	private Long dateAcceptance;

	private String fees;

	@OneToOne
	@JoinColumn(name = "channel_attribute_id")
	private ChannelAttribute channelAttribute;

	public ChannelDetail(String routerType, String deviceStatus, String votesRequire, String ipType,
			String regionalEngineer, Long deployRequestDate,
			Long dateAcceptance, String ipAddress, String fees) {
		this.routerType = routerType;
		this.votesRequire = votesRequire;
		this.ipType = ipType;
		this.deviceStatus = deviceStatus;
		this.regionalEngineer = regionalEngineer;
		this.deployRequestDate = deployRequestDate;
		this.dateAcceptance = dateAcceptance;
		this.ipAddress = ipAddress;
		this.fees = fees;
	}

}
