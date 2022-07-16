package vn.tpsc.it4u.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ConfigTokenSummary {
	private Long id;
	private String csrfToken;
	private String unifises;
}
