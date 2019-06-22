package net.bigmir.exceptions;

public class UserBannedException extends RuntimeException {
    private String message;

    public UserBannedException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
