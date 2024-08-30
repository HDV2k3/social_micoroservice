package com.example.connect_service.Dto;

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
    private int code = 1000;
    private String message;
    private T result;
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(101000)
                .result(data)
                .message("Successfully")
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return ApiResponse.<T>builder()
                .code(101001)
                .result(null)
                .message(errorMessage)
                .build();
    }
}
