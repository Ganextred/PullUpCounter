package com.example.pullupcounter.config;

import com.example.pullupcounter.model.security.CustomOAuth2User;
import com.example.pullupcounter.model.security.CustomOAuth2UserService;
import com.example.pullupcounter.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/apartments", "/apartment/","/register",
                        "/static/**","/lang", "/apartments/applySort","/upload/**", "/api/**", "/oauth/**", "/static/image/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth2/authorization/google")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        System.out.println("AuthenticationSuccessHandler invoked");
                        System.out.println("Authentication name: " + authentication.getName());
                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                        userService.processOAuthPostLogin(oauthUser.getEmail());

                        response.sendRedirect("/account");
                    }
                })
                //.defaultSuccessUrl("/list")
                .and()
                .logout().logoutUrl("/logout")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
        ;
    }

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private UserService userService;
}
