package net.bigmir;

import net.bigmir.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier(value = "getUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder managerBuilder) throws Exception{
        managerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public UserDetailsService getUserDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                enabling csrf security
                .authorizeRequests()
                  .antMatchers("/")
                  .hasAnyRole("USER","ADMIN")
                  .antMatchers("/admin")
                  .hasAnyRole("ADMIN")
                  .antMatchers("/sign_up", "/error**")
                  .permitAll()
                .and()
                  .exceptionHandling()
                  .accessDeniedPage("/forbidden")
                .and()
                  .formLogin()
                  .loginPage("/login")
                  .loginProcessingUrl("/security_check")
                  .failureUrl("/login?error")
                  .usernameParameter("email")
                  .passwordParameter("password")
                  .permitAll()
                .and()
//                OAuth2 authorization
                .oauth2Login()
                  .loginPage("/login")
                  .successHandler(authenticationSuccessHandler)
                .and()
                  .logout()
                  .permitAll()
                  .logoutUrl("/logout")
                  .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);
    }

}
