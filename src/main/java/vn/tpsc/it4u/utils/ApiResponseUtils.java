package vn.tpsc.it4u.utils;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.tpsc.it4u.payloads.ApiResponse;

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

	/**
	 * 
	 * @param response       HttpServletResponse
	 * @param responseStatus response Status
	 * @param errors         errorObject
	 * @throws IOException
	 */
	public void responseServletError(HttpServletResponse response, HttpStatus responseStatus, Exception e)
			throws IOException {
		ApiResponse responseObject = new ApiResponse();
		responseObject.setCode(responseStatus.value());
		responseObject.setMessage(messageSource.getMessage(String.valueOf(responseStatus.value()), null, "Error", null));
		responseObject.setErrors(e.getMessage());

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String result = mapper.writeValueAsString(responseObject);

		response.setStatus(responseStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(result);
	}
}