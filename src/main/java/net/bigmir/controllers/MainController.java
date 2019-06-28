package net.bigmir.controllers;

import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import javassist.NotFoundException;
import net.bigmir.dto.SimpleUserDTO;
import net.bigmir.dto.StartUpDTO;
import net.bigmir.dto.result.BadRequestResult;
import net.bigmir.dto.result.ResultDTO;
import net.bigmir.dto.result.SuccessResult;
import net.bigmir.exceptions.UserBannedException;
import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.model.UserRole;
import net.bigmir.services.FriendService;
import net.bigmir.services.SimpleUserService;
import net.bigmir.services.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class MainController {

    @Autowired
    private StartUpService startUpService;

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FriendService friendService;

    @RequestMapping("/")
    public String index(Model model, Authentication authentication) {
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        if (user.getRole().equals(UserRole.ADMIN)) {
            model.addAttribute("admin", true);
        }
        if (user.getRole().equals(UserRole.BANNED)){
            long date = new Date().getTime();
            if (date<(user.getExpiredTime().getTime()+1000*60*60*24)) {
//                banning user for 24 hours
                throw new UserBannedException("You are banned for 24 hours by administrator");
            }else {
                user.setRole(UserRole.USER);
                simpleUserService.saveUser(user);
            }
        }
        return "index";
    }

    @RequestMapping("/myprofile")
    public String updateMyProfile(Model model, Authentication authentication) {
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("photo", user.getPictureURL());
        return "myprofile";

    }

    @RequestMapping("/set_photo")
    public String setPhoto(@RequestParam(name = "file") MultipartFile file, Authentication authentication) {
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        String photo = user.getEmail() +  ".png";
        String path = "photos/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        byte[] bytes = new byte[0];
        if (file!=null) {
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File p = new File(path + photo);
            try (OutputStream os = new FileOutputStream(p)) {
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            user.setPictureURL("photos/" + photo);
            simpleUserService.saveUser(user);
            if (friendService.isFriendExists(user.getEmail())){
                friendService.getFriend(user.getEmail()).setPictureURL("photos/" + photo);
                friendService.save(friendService.getFriend(user.getEmail()));
            }
        }
        return "redirect:/myprofile";

    }
    //for showing avatar pictures from different urls
    @RequestMapping("{end}/{end}/photos/{name}")
    public ResponseEntity<byte []> getPhotoFromMoreEnds(@PathVariable("name") String name){
        return getBytes(name);
    }

    @RequestMapping("{end}/photos/{name}")
    public ResponseEntity<byte []> getPhotoFromAnotherEnds(@PathVariable("name") String name){
        return getBytes(name);
    }

    @RequestMapping("/photos/{name}")
    public ResponseEntity<byte []> getPhotoFromMain(@PathVariable("name") String name){
        return getBytes(name);
    }


    @RequestMapping("/updateProfile")
    public String updateProfile(@RequestParam("login") String login,
                                @RequestParam("phone") String phone,
                                @RequestParam("password") String password,
                                Authentication authentication){
        System.out.println(login.length());
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        if((!login.equals(""))&&login.length()>3){
            user.setLogin(login);
        }

        if ((!phone.equals(""))){
            user.setPhone(phone);
        }
        if ((!password.equals(""))&&password.length()>=6){
            String cryptPass = passwordEncoder.encode(password);
            user.setPassword(cryptPass);
        }

        simpleUserService.saveUser(user);
        if (friendService.isFriendExists(user.getEmail())){
            Friend friend = friendService.getFriend(user.getEmail());
            if((!login.equals(""))&&login.length()>3){
                friend.setLogin(login);
            }

            if ((!phone.equals(""))){
                friend.setPhone(phone);
            }
            friendService.save(friend);
        }
        return "redirect:/myprofile";
    }

    @RequestMapping("/colleg")
    public String colleg(Authentication authentication, Model model){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        Set<Friend> friends = simpleUserService.getFriends(user);
        if (!friends.isEmpty()){
            model.addAttribute("friends", friends);
        }
        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        return "colleg";
    }

    @RequestMapping("/invite")
    public String invite(@RequestParam("email") String email, HttpServletRequest request, Authentication authentication,Model model){
        SimpleUser simpleUser = simpleUserService.getSimpleUserFromAuth(authentication);
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        SimpleMailMessage mail = constructInvitationEmail(appUrl,simpleUser,email);
        mailSender.send(mail);
        model.addAttribute("success", "Invitation is successfully sent");
        model.addAttribute("login", simpleUser.getLogin());
        model.addAttribute("photo", simpleUser.getPictureURL());
        return "friends";

    }

    @RequestMapping("/create_startup")
    public String startUps(Authentication authentication, Model model){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        List<StartUpDTO> dtos = new ArrayList<>();
        Set<StartUp> startUps = startUpService.getMyStartUps(user);
        startUps.forEach(x -> dtos.add(x.toDTO()));

        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("startups",dtos);
        return "startups";
    }

    @RequestMapping("/admin")
    public String admin(Authentication authentication, Model model, @Param("word") String word){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        List<SimpleUser> users = null;
        if (word==null) {
             users = simpleUserService.findAll();
        }else {
            users = simpleUserService.getUsersLike(word);
        }
        List<SimpleUserDTO> userDTOS = new ArrayList<>();
        users.remove(user);
        users.forEach(s -> userDTOS.add(s.toDTO()));
        model.addAttribute("users", userDTOS);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        return "admin";
    }


    @RequestMapping("/block")
    public ResponseEntity<ResultDTO> deleteUser(@RequestParam("json") String json, Authentication authentication){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        if(user.getRole().equals(UserRole.ADMIN)){
            Gson gson = new Gson();
            String email = gson.fromJson(json, String.class);
            simpleUserService.blockUser(simpleUserService.findByEmail(email));

        }
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/forbidden")
    public String denyPage(){
        return "forbidden";
    }

//for reading avatar pictures
    private ResponseEntity<byte []> getBytes(String name){
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get("photos/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
//creating invitation email
    private SimpleMailMessage constructInvitationEmail(
            String contextPath,SimpleUser user, String email) {
        String url = contextPath + "/login";
        String message = "Hello dear collegue, I'm " + user.getLogin() + " \n" +
                "Let's work together for creating StartUp, \n" +
                "Please sign up in the StartUpApp";
        return constructEmail("New StartUp", message + " \r\n" + url, email);
    }

    private SimpleMailMessage constructEmail(String subject, String body, String email) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(body);
        mail.setTo(email);
        mail.setFrom("startup@smth.com");
        return mail;
    }

    @ExceptionHandler(UserBannedException.class)
    public String handleException(UserBannedException exception, Model model, Authentication authentication){
//       setting user logged out of app
        authentication.setAuthenticated(false);
        model.addAttribute("userBanned", exception.getMessage());
        return "error";
    }
}
