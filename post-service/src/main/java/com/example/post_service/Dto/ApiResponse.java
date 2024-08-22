package com.example.post_service.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private int code = ApiResponseCode.SUCCESS.getCode(); // Default code

    private String message = ApiResponseCode.SUCCESS.getMessage(); // Default message
    private T result;

    // Optional: Add a static method to create response with predefined codes
    public static <T> ApiResponse<T> of(ApiResponseCode responseCode, T result) {
        return ApiResponse.<T>builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> of(ApiResponseCode responseCode) {
        return ApiResponse.<T>builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();
    }
}
