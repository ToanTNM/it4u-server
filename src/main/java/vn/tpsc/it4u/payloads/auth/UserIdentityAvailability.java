package vn.tpsc.it4u.payloads.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserIdentityAvailability {
	private Boolean available;
}
