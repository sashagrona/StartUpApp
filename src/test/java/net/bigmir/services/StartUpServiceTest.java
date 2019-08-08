package net.bigmir.services;

import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.model.UserRole;
import net.bigmir.repositories.ChatRepository;
import net.bigmir.repositories.StartUpRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StartUpServiceTest {

    @Autowired
    private StartUpService startUpService;

    @MockBean
    private StartUpRepository startUpRepository;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private SimpleUserService simpleUserService;

    @Test
    public void createNewStartUp() {
        StartUp startUp = new StartUp().of("name", "idea");
        SimpleUser user = new SimpleUser("App", "steam@gmail.com", "+380961234567", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.USER);
        startUpService.createNewStartUp(startUp, user);
        Assert.assertNotNull(startUp.getPlan());
        Mockito.verify(startUpRepository, Mockito.times(1)).save(startUp);


    }

    @Test
    public void updateStartUp() {

        StartUp startUp = new StartUp().of("name", "idea");
        startUpRepository.save(startUp);
        startUp.setIdea("notIdea");
        Mockito.verify(startUpRepository, Mockito.times(1)).save(startUp);
        Mockito.when(startUpRepository.findByName("name")).thenReturn(startUp);
        Assert.assertNotEquals("idea", startUpService.findByName("name").getIdea());
    }


    @Test
    public void getMyStartUps() {
        SimpleUser user = new SimpleUser("App", "steam@gmail.com", "+380961234567", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.USER);

        Mockito.when(startUpRepository.findAllByUser(user)).thenReturn(new HashSet<>(Arrays.asList(new StartUp(), new StartUp(), new StartUp())));
        Assert.assertEquals(3, startUpService.getMyStartUps(user).size());
    }

    @Test
    public void findByName() {
        Mockito.when(startUpRepository.findByName("name")).thenReturn(new StartUp().of("name", "idea"));
        Assert.assertEquals("name", startUpService.findByName("name").getName());
    }
}