package net.bigmir.controllers;

import net.bigmir.repositories.SimpleUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTestIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginController loginController;

    @Autowired
    private SimpleUserRepository repository;


    @Test
    public void ping() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @Test
    public void login() throws Exception{
        this.mockMvc
                .perform(formLogin("/security_check").user("sasha_grona@bigmir.net")
                        .password("password")
                        .userParameter("email")
                        .passwordParam("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void signUp() throws Exception {
        this.mockMvc
                .perform(post("/sign_up").with(csrf()).param("login","log123")
                        .param("email", "ema@df.net")
                        .param("password", "pass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}