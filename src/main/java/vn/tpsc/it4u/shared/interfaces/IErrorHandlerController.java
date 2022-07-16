package vn.tpsc.it4u.shared.interfaces;

import org.springframework.http.ResponseEntity;

public interface IErrorHandlerController {
	ResponseEntity<String> error();

	ResponseEntity<String> exception(Exception exception);
}