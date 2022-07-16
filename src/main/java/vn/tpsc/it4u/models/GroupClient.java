package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * group_client
 */
@Getter
@Setter
@NoArgsConstructor
// @Document("sitesname")
@Entity
@Table(name = "group_client")

public class GroupClient {

	/**
	 *
	 */
	// private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(max = 40)
	private String groupName;

	public GroupClient(String groupName) {
		this.groupName = groupName;
	}
}