package net.bigmir.services;

import net.bigmir.dto.SimpleUserDTO;
import net.bigmir.model.Friend;
import net.bigmir.model.PasswordResetToken;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.UserRole;
import net.bigmir.repositories.PasswordResetTokenRepository;
import net.bigmir.repositories.SimpleUserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleUserServiceTest {

    @Autowired
    private SimpleUserService simpleUserService;

    @MockBean
    private SimpleUserRepository simpleUserRepository;

    @MockBean
    private StartUpService startUpService;

    @MockBean
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    public void addUserByRegistration() {
        SimpleUser user = new SimpleUser("example","smsh@vv.net","hash123",null,null, UserRole.USER);
        boolean isCreated = simpleUserService.addUserByRegistration(user);
        Assert.assertTrue(isCreated);
        Mockito.verify(simpleUserRepository,Mockito.times(1)).save(user);

    }

    @Test
    public void getUsersLike() {
        String word = "user";
        SimpleUser userOne = new SimpleUser();
        SimpleUser userTwo = new SimpleUser();
        userOne.setLogin("user1");
        userTwo.setLogin("user2");
        Mockito.when(simpleUserRepository.getUsersLike(word)).thenReturn(new ArrayList<>(Arrays.asList(userOne,userTwo)));
        Assert.assertEquals(2,simpleUserService.getUsersLike(word).size());
    }

    @Test
    public void findUserByToken() {
        SimpleUser user = new SimpleUser();
        String tokenString = "1234";
        PasswordResetToken token = new PasswordResetToken();
        token.setPassToken(tokenString);
        token.setSimpleUser(user);
        Mockito.when(passwordResetTokenRepository.findByPassToken(tokenString)).thenReturn(token);
        Assert.assertEquals(user, simpleUserService.findUserByToken(tokenString));
    }

    @Test
    public void findAll() {
        SimpleUser userOne = new SimpleUser();
        SimpleUser userTwo = new SimpleUser();
        SimpleUser userThree = new SimpleUser();
        userOne.setEmail("user1@bigmir.net");
        userTwo.setEmail("user2@bigmir.net");
        userThree.setEmail("user3@bigmir.net");
        Mockito.when(simpleUserRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(userOne,userTwo,userThree)));
        Assert.assertEquals(3, simpleUserService.findAll().size());
    }

    @Test
    public void findAllEmails() {
        String [] array = new String[]{"user1@bigmir.net","user2@bigmir.net","user3@bigmir.net"};

        Mockito.when(simpleUserRepository.findAllEmails()).thenReturn(new ArrayList<>(Arrays.asList(array)));
        Assert.assertEquals(3,simpleUserService.findAllEmails().size());
    }

    @Test
    public void getFriends() {
        SimpleUser user = new SimpleUser();
        user.setEmail("user1@bigmir.net");
        Mockito.when(simpleUserRepository.findFriends(user)).thenReturn(new HashSet<>(Arrays.asList(new Friend(), new Friend(), new Friend())));
        Assert.assertEquals(3,simpleUserService.getFriends(user).size());
    }
}