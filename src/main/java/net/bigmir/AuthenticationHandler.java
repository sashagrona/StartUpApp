package net.bigmir;

import net.bigmir.dto.SimpleUserDTO;
import net.bigmir.model.UserRole;
import net.bigmir.services.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SimpleUserService simpleUserService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = token.getPrincipal();

        Map<String, Object> attrs = user.getAttributes();
        String id = String.valueOf(attrs.get("id"));
        String pictureUrl = (String) attrs.get("picture");
        String gitPicture = (String) attrs.get("avatar_url");
        if (pictureUrl == null && gitPicture == null) {
            pictureUrl = "https://graph.facebook.com/v3.2/" + id + "/picture";
        } else if (gitPicture != null) {
            pictureUrl = gitPicture;
        }
//        if can't get user email during authorization causing by account settings
        if (attrs.get("email") == null) {
            httpServletResponse.sendRedirect("/set_email");
        } else {

            SimpleUserDTO simpleUserDTO = SimpleUserDTO.of((String) attrs.get("name"), (String) attrs.get("email"), (String) attrs.get("phone"), pictureUrl, UserRole.USER);

            simpleUserService.addUserVia(simpleUserDTO);

            httpServletResponse.sendRedirect("/");
        }
    }
}
