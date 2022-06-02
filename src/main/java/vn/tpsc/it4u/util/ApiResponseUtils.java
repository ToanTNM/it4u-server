package vn.tpsc.it4u.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import vn.tpsc.it4u.payload.ApiResponse;

/**
 * ApiResponseUtils
 */
@Component
public class ApiResponseUtils {

	@Autowired
	private MessageSource messageSource;

	public ApiResponse success(int code, Locale locale) {
		ApiResponse response = new ApiResponse();
		response.setCode(code);
		response.setMessage(messageSource.getMessage(String.valueOf(code), null, "Success", locale));

		return response;
	}

	public ApiResponse success(String message) {
		ApiResponse response = new ApiResponse();
		response.setCode(1000);
		response.setMessage(message);

		return response;
	}

	public ApiResponse success(Object data, Locale locale) {
		ApiResponse response = new ApiResponse();
		response.setCode(1000);
		response.setMessage(messageSource.getMessage(String.valueOf(1000), null, "Success", locale));
		response.setData(data);

		return response;
	}

	public ApiResponse error(int code, Locale locale) {
		ApiResponse response = new ApiResponse();
		response.setCode(code);
		response.setMessage(messageSource.getMessage(String.valueOf(code), null, "Error", locale));

		return response;
	}

	public ApiResponse error(int code, Object errors, Locale locale) {
		ApiResponse response = new ApiResponse();
		response.setCode(code);
		response.setMessage(messageSource.getMessage(String.valueOf(code), null, "Error", locale));
		response.setErrors(errors);

		return response;
	}
}