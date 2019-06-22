package net.bigmir;

import lombok.Data;

@Data
public class GenericResponse {
    private String message;
    private int error;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(String message, int error) {
        this.message = message;
        this.error = error;
    }
}
