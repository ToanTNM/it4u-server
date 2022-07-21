package vn.tpsc.it4u.models.channel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

/**
 * ChannelName
 */
@Data
@Entity
@Table(name = "channelValue")
@Builder
public class ChannelValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 40)
	private String servicePack;

	@NotEmpty
	@Size(max = 40)
	private String value;

	@OneToOne
	@JoinColumn(name = "channelName_id")
	private ChannelName channelName;

	// public ChannelValue(String servicePack, String value, ChannelName
	// channelName) {
	// this.servicePack = servicePack;
	// this.value = value;
	// this.channelName = channelName;
	// }
}
