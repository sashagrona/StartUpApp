package net.bigmir.controllers;

import net.bigmir.dto.result.BadRequestResult;
import net.bigmir.dto.result.ResultDTO;
import net.bigmir.exceptions.EmailNotFoundException;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.UserRole;
import net.bigmir.services.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    private static String authorizationRequestBaseUri = "oauth2/authorization";

    private Map<String, String> urls = new HashMap<>();

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;


    @RequestMapping("/login")
    public String login(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
//        to put into map urls for OAuth2 login
        clientRegistrations.forEach(register -> urls.put(register.getClientName(), authorizationRequestBaseUri + "/" + register.getRegistrationId()));
        model.addAttribute("google", urls.get("Google"));
        model.addAttribute("facebook", urls.get("Facebook"));
        model.addAttribute("github", urls.get("GitHub"));
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        List<String> emails = simpleUserService.findAllEmails();
        model.addAttribute("emails", emails);
        return "register";
    }

    @RequestMapping("/set_email")
    public void throwException() {
        throw new EmailNotFoundException("StartUpApp needs to know your email<br>\n" +
                "    For some reasons we can't verify your email<br>\n" +
                "    Please, check access rights for your email at your account which from you wanna enter<br>\n" +
                "    and try again");
    }

    @RequestMapping("/sign_up")
    public String signUp(@RequestParam String login,
                         @RequestParam String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam String password) {
//        to encode password by BCrypt
        String hashPass = passwordEncoder.encode(password);
        if (simpleUserService.isNotAuthorized(login)) {
            SimpleUser simpleUser = new SimpleUser(login, email, phone, hashPass, null, UserRole.USER);
            simpleUserService.addUserByRegistration(simpleUser);

        }
        return "redirect:/";
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public String handleException(EmailNotFoundException exception, Model model, Authentication authentication) {
        authentication.setAuthenticated(false);
        model.addAttribute("notFoundEmail", exception.getMessage());
        return "error";
    }

}
