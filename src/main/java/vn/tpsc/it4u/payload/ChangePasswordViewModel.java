package vn.tpsc.it4u.payload;

import lombok.Data;

/**
 * ChangePasswordViewModel
 */
@Data
public class ChangePasswordViewModel {

    private String newPassword;

    private String currentPassword;

    private String confirmPassword;
}