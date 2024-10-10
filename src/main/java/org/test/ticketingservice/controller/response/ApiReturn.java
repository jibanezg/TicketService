package org.test.ticketingservice.controller.response;


import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the API call result. It bundles a potential message to the client,
 * the requested data (if any) and a list of potential errors (if any). For consistency
 * and proper standards this class was defined and used throughout the application.
 *
 * @param <T> Any domain object or value that the endpoint returns.
 */
public class ApiReturn<T> {
    private String message;
    private T data;
    private List<String> errors;

    // Constructors
    public ApiReturn(String message, T data) {
        this.message = message;
        this.data = data;
        this.errors = new ArrayList<>();
    }

    public ApiReturn(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
        this.data = null;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
