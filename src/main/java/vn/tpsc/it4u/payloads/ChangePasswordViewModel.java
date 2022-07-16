package vn.tpsc.it4u.payloads;

import lombok.Data;

/**
 * ChangePasswordViewModel
 */
@Data
public class ChangePasswordViewModel {

	private String currentPassword;

	private String newPassword;

	private String confirmPassword;
}