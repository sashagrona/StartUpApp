package net.bigmir.dto;

import lombok.Getter;
import lombok.Setter;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.UserRole;

@Getter
@Setter
public class SimpleUserDTO {
    private final String login;
    private final String email;
    private final String phone;
    private final String pictureURL;

    //to find out if button for adding would be enabled or disabled
    private boolean disableButton;
    private UserRole role;

    private SimpleUserDTO(String login, String email, String phone, String pictureURL, UserRole role) {
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.pictureURL = pictureURL;
        this.role=role;
    }

    public static SimpleUserDTO of(String login, String  email, String phone, String pictureURL, UserRole role){
        return new SimpleUserDTO(login,email,phone,pictureURL, role);
    }

}
