package net.bigmir.controllers;

import net.bigmir.repositories.SimpleUserRepository;
import net.bigmir.services.SimpleUserService;
import net.bigmir.services.StartUpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "sasha_grona@bigmir.net", userDetailsServiceBeanName = "getUserDetailsService")
public class MainControllerTestIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    @MockBean
    private StartUpService startUpService;

    @Test
    public void index() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/create_startup"));
    }

    @Test
    @WithUserDetails(value = "petya_smth@smth.ua", userDetailsServiceBeanName = "getUserDetailsService")
    public void admin() throws Exception{
        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminCorrect() throws Exception{
        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("login", "Sasha Grona"));
    }

}