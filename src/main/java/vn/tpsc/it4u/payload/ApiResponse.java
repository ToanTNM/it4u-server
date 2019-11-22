package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ApiResponse
 */
@Data
@AllArgsConstructor
public class ApiResponse {

    private Boolean success;
    private String message;
}