package vn.tpsc.it4u.payloads.auth;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
public class LogOutRequest {

	private Long userId;
}
