package net.bigmir.services;

import net.bigmir.dto.SimpleUserDTO;
import net.bigmir.model.*;
import net.bigmir.repositories.PasswordResetTokenRepository;
import net.bigmir.repositories.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SimpleUserService {

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Transactional
    public SimpleUser findByEmail(String email){
       return simpleUserRepository.findByEmail(email);
    }

    @Transactional
    public void blockUser(SimpleUser user){
       user.setRole(UserRole.BANNED);
       user.setExpiredTime(new Date());
       simpleUserRepository.save(user);
    }

    @Transactional
    public boolean isNotAuthorized(String email){
        if(simpleUserRepository.existsByEmail(email)){
            return false;
        }
        return true;
    }

    @Transactional
    public void addUserVia(SimpleUserDTO simpleUserDTO){
        if(simpleUserDTO.getEmail()==null || simpleUserRepository.existsByEmail(simpleUserDTO.getEmail())){
            return;
        }
        SimpleUser simpleUser = SimpleUser.fromDTO(simpleUserDTO);
        simpleUserRepository.save(simpleUser);
    }

    @Transactional
    public List<SimpleUser> getUsersLike(String word){
        return simpleUserRepository.getUsersLike(word);
    }

    @Transactional
    public boolean addUserByRegistration(SimpleUser simpleUser){
        if (simpleUserRepository.existsByEmail(simpleUser.getEmail())){
            return false;
        }
        simpleUserRepository.save(simpleUser);
        return true;
    }

    public void createPasswordResetToken(SimpleUser simpleUser, String token){
        PasswordResetToken myToken = new PasswordResetToken(token,simpleUser);
        myToken.setExpireDate(new Date());
        passwordResetTokenRepository.save(myToken);
    }

    public String validatePasswordToken(long id, String token){
        PasswordResetToken tokenPass = passwordResetTokenRepository.findByPassToken(token);
        if(tokenPass == null || tokenPass.getSimpleUser().getId()!=id){
            return "invalidToken";
        }
        Calendar calendar = Calendar.getInstance();
        if(tokenPass.getExpireTime() - calendar.getTime().getTime() <= 0){
            return "expired";
        }
        return null;
    }

    public SimpleUser findUserByToken(String token){
        return passwordResetTokenRepository.findByPassToken(token).getSimpleUser();
    }

    @Transactional
    public List<SimpleUser> findAll(){
        return simpleUserRepository.findAll();
    }

    @Transactional
    public SimpleUser getSimpleUserFromAuth(Authentication authentication){
        String email = null;
        String regex = "\\w+([\\.-]?\\w+)*@(((([a-z0-9]{2,})|([a-z0-9][-][a-z0-9]+))[\\.][a-z0-9])|([a-z0-9]+[-]?))+[a-z0-9]+\\.([a-z]{2}|(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum))";

        Pattern pattern = Pattern.compile(regex);
        Matcher mat = pattern.matcher(authentication.getName());
        //To define in which way user was authorized(OAuth2/Simple registration)
        //In OAuth2 method authentication.getName() always return long
        if (mat.matches()){
            email = authentication.getName();
        }else{
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User user = token.getPrincipal();
            email= (String) user.getAttributes().get("email");
        }
        return findByEmail(email);
    }

    @Transactional
    public void saveUser(SimpleUser simpleUser){
        simpleUserRepository.save(simpleUser);
    }

    @Transactional
    public List<String> findAllEmails(){
       return simpleUserRepository.findAllEmails();
    }

    @Transactional
    public Set<Friend> getFriends(SimpleUser user){
        return simpleUserRepository.findFriends(user);
    }


    @Transactional
    public boolean ifUserBelongsChat(Chat chat, SimpleUser simpleUser){
        List<SimpleUser> users = simpleUserRepository.getUsersFromChat(chat);
        for (SimpleUser user: users) {
            if (user.getEmail().equals(simpleUser.getEmail())){
                return true;
            }
        }
        return false;
    }

}
