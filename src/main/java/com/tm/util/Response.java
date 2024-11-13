package com.tm.util;

public class Response<T> {
    private String responseType;
    private String message;
    private String errorMessage;
    private T data;

    public Response(String responseType, String message, String errorMessage, T data) {
        this.responseType = responseType;
        this.message = message;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    // Constructor without error
    public Response(String responseType, String message, T data) {
        this(responseType, message, null, data);
    }

    // Constructor without data or error
    public Response(String responseType, String message) {
        this(responseType, message, null, null);
    }

    // Constructor without data
    public Response(String responseType, String message, String errorMessage) {
        this(responseType, message, errorMessage, null);
    }

    // Getters and Setters
    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}