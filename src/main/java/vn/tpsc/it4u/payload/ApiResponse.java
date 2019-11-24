package vn.tpsc.it4u.payload;

import lombok.Data;

/**
 * ApiResponse
 */
@Data
public class ApiResponse {
    private int code;
    
    private String message;

    private Object data;

    private Object errors;
}