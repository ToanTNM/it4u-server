package vn.tpsc.it4u.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SystemConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String usernameUbnt;

	@NotEmpty
	private String pwUbnt;

	@NotEmpty
	private String tokenDev;

	@NotEmpty
	private String tokenUcrm;

	public SystemConfig(String usernameUbnt, String pwUbnt, String tokenDev,
			String tokenUcrm) {
		this.usernameUbnt = usernameUbnt;
		this.pwUbnt = pwUbnt;
		this.tokenDev = tokenDev;
		this.tokenUcrm = tokenUcrm;
	}

}
