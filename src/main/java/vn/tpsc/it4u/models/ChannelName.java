package vn.tpsc.it4u.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * ChannelName
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "channelName")
public class ChannelName {
	/**
	*
	*/
	// private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 40)
	private String name;

	public ChannelName(String name) {
		this.name = name;
	}
}
