package net.bigmir.controllers;

import net.bigmir.GenericResponse;
import net.bigmir.exceptions.InvalidTokenException;
import net.bigmir.model.SimpleUser;
import net.bigmir.services.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/forgot")
public class ForgotPasswordController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/showPage")
    public String showForgotPage() {
        return "forgot_password";
    }



    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(Model model, HttpServletRequest request, @RequestParam("email") String email) {
        SimpleUser simpleUser = simpleUserService.findByEmail(email);
        if (simpleUser == null) {
            throw new UsernameNotFoundException("User not found via this email");
        }else {
            String token = UUID.randomUUID().toString();
            simpleUserService.createPasswordResetToken(simpleUser, token);
//            sending message via email
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            SimpleMailMessage mail = constructResetTokenEmail(appUrl, token, simpleUser);
            mailSender.send(mail);
            model.addAttribute("success", new GenericResponse("Please, check your email"));
        }
        return "forgot_password";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String showChangePage(Model model,@RequestParam("id") long id, @RequestParam("token") String token){
       String result = simpleUserService.validatePasswordToken(id,token);
       if (result !=null){
           throw new InvalidTokenException("Your password can't be updated because of invalid token or expired date");
       }
       model.addAttribute("token",token);
       return "update_password";
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updateNewPassword(@RequestParam("password") String password, @RequestParam("token") String token){

            SimpleUser simpleUser = simpleUserService.findUserByToken(token);
            simpleUser.setPassword(passwordEncoder.encode(password));
            simpleUserService.saveUser(simpleUser);
            return "redirect:/login";
    }
//creating content of email
    private SimpleMailMessage constructResetTokenEmail(
            String contextPath, String token, SimpleUser user) {
        String url = contextPath + "/forgot/changePassword?id=" +
                user.getId() + "&token=" + token;
        String message = "Reset your password";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             SimpleUser user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("startup@smth.com");
        return email;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleException(UsernameNotFoundException exception, Model model) {
        model.addAttribute("userNotFound", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidTokenException.class)
    public String handleToken(InvalidTokenException exception, Model model){
        model.addAttribute("invalidToken", exception.getMessage());
        return "error";
    }

}
