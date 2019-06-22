package net.bigmir.exceptions;

public class FileOverMaximumException extends RuntimeException {
    private String message;

    public FileOverMaximumException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
