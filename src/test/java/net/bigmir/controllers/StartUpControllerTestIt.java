package net.bigmir.controllers;

import com.google.gson.Gson;
import net.bigmir.dto.StartUpDTO;
import net.bigmir.model.Chat;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.model.UserRole;
import net.bigmir.repositories.ChatRepository;
import net.bigmir.repositories.SimpleUserRepository;
import net.bigmir.repositories.StartUpRepository;
import net.bigmir.services.ChatService;
import net.bigmir.services.SimpleUserService;
import net.bigmir.services.StartUpService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StartUpControllerTestIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StartUpService startUpService;

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private StartUpController startUpController;

    @MockBean
    private StartUpRepository startUpRepository;

    @MockBean
    private SimpleUserRepository simpleUserRepository;

    @MockBean
    private ChatRepository chatRepository;

    @Test
    @WithUserDetails(value = "sasha_grona@bigmir.net", userDetailsServiceBeanName = "getUserDetailsService")
    public void addStartUp() throws Exception{
        StartUpDTO dto = new StartUpDTO("name","idea");
        Gson gson = new Gson();
        String json=gson.toJson(dto);
        this.mockMvc.perform(post("/startup/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        Assert.assertEquals(startUpService.findByName(dto.getName()).getCreator().getEmail(), simpleUserService.findByEmail("sasha_grona@bigmir.net").getEmail());
        Assert.assertNotNull(startUpService.findByName(dto.getName()).getPlan());

    }

    @Test
    public void addParticipant() throws Exception{

        String email = "friend@smth.net";
        Gson gson = new Gson();
        String json = gson.toJson(email);
        Mockito.when(startUpRepository.findByName("name")).thenReturn(new StartUp().of("name", "idea"));
        Mockito.when(simpleUserRepository.findByEmail(email)).thenReturn(new SimpleUser("user", email, "+380961234567", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.USER));
        Mockito.when(chatRepository.getChatByName("name")).thenReturn(new Chat("name"));

        this.mockMvc.perform(post("/startup/name/add_participant")
                .with(csrf())
                .param("json", json))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertNotNull(chatService.getChatByName("name"));
        Assert.assertNotNull(simpleUserService.findByEmail(email));
        Assert.assertNotNull(startUpService.findByName("name"));

    }


}