package net.bigmir.controllers;

import com.google.gson.Gson;
import net.bigmir.dto.SimpleUserDTO;
import net.bigmir.dto.result.ResultDTO;
import net.bigmir.dto.result.SuccessResult;
import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.services.FriendService;
import net.bigmir.services.SimpleUserService;
import net.bigmir.services.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private StartUpService startUpService;

    @RequestMapping("/find")
    public String findFriend(Authentication authentication, Model model) {
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        List<SimpleUser> users = simpleUserService.findAll();
        List<SimpleUserDTO> userDTOS = new ArrayList<>();
        users.remove(user);
        users.forEach(s -> userDTOS.add(s.toDTO()));
        for (SimpleUserDTO dto:userDTOS) {
            if (friendService.isFriend(user, dto.getEmail())){
                dto.setDisableButton(true);
            }else {
                dto.setDisableButton(false);
            }
        }

        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        model.addAttribute("users", userDTOS);
        return "friends";
    }

    @RequestMapping("/profile")
    public String showProfile(@RequestParam("email") String email, Authentication authentication, Model model){
         SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
         Friend friend = new Friend(simpleUserService.findByEmail(email));
//         to prohibit users for getting private info, who don't belong to friends list
         if (friendService.isFriend(user,friend.getEmail())||friend.getEmail().equals(user.getEmail())) {
             model.addAttribute("photo", user.getPictureURL());
             model.addAttribute("login", user.getLogin());
             model.addAttribute("friend", friend);
             return "friend_profile";
         }else {
             return "redirect:/forbidden";
         }
    }

    @RequestMapping("/show")
    public String showFriends(@RequestParam("startUp") String startUpName, Authentication authentication, Model model){
        StartUp startUp = startUpService.findByName(startUpName);
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        Set<Friend> friends = simpleUserService.getFriends(user);
        List<SimpleUser> users = new LinkedList<>();
        for (Friend f:friends) {
            users.add(simpleUserService.findByEmail(f.getEmail()));
        }
        List<SimpleUserDTO> userDTOS = new ArrayList<>();
        users.forEach(s -> userDTOS.add(s.toDTO()));
        for (SimpleUserDTO dto:userDTOS) {
            if (friendService.isParticipant(startUp, dto.getEmail())){
                dto.setDisableButton(true);
            }else {
                dto.setDisableButton(false);
            }
        }

        model.addAttribute("startUp", startUpName);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        if (userDTOS.isEmpty()){
            model.addAttribute("users", null);
        }else {
            model.addAttribute("users", userDTOS);
        }
        return "startupfriends";
    }

    @RequestMapping("/add")
    public ResponseEntity<ResultDTO> addFriend(@RequestParam("json") String jsonEmail, Authentication authentication) {
        Gson gson = new Gson();
        String email = gson.fromJson(jsonEmail, String.class);
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        SimpleUser userFriend = simpleUserService.findByEmail(email);
        if (!friendService.isFriend(user,userFriend.getEmail())) {
            if (!friendService.isFriendExists(email)) {
                user.addFriend(new Friend(userFriend));
            }else {
                user.addFriend(friendService.getFriend(email));
            }
            simpleUserService.saveUser(user);
        }
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/delete")
    public ResponseEntity<ResultDTO> deleteFriend(@RequestParam("json") String jsonEmail, Authentication authentication){
        Gson gson = new Gson();
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        SimpleUser userFriend = simpleUserService.findByEmail(gson.fromJson(jsonEmail, String.class));
        friendService.deleteFriendByUser(user, userFriend.getEmail());

        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/search")
    public String searchFriend(@RequestParam("word") String word, Model model, Authentication authentication){
        List<SimpleUser> users = simpleUserService.getUsersLike(word);
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        List<SimpleUserDTO> userDTOS = new ArrayList<>();
        users.remove(user);
        users.forEach(s -> userDTOS.add(s.toDTO()));
        for (SimpleUserDTO dto:userDTOS) {
            if (friendService.isFriend(user, dto.getEmail())){
                dto.setDisableButton(true);
            }else {
                dto.setDisableButton(false);
            }
        }

        model.addAttribute("users", userDTOS);
        model.addAttribute("login", user.getLogin());
        model.addAttribute("photo", user.getPictureURL());
        return "friends";
    }
}
