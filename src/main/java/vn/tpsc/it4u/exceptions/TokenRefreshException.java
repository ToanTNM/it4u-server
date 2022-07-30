package vn.tpsc.it4u.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

	public TokenRefreshException(String token, String message) {
		super(String.format("[%s]: %s", token, message));
	}
}
