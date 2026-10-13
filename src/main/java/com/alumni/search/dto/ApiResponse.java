package com.alumni.search.dto;

public class ApiResponse<T> {

    private String status;
    private T data;

    private ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data);
    }

    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>("error", data);
    }

    public String getStatus() { return status; }
    public T getData() { return data; }
}
