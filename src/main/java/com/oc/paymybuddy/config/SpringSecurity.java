package com.oc.paymybuddy.config;

import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/users").hasAuthority(Role.USER.name())
                                .requestMatchers("/editProfile").hasAuthority(Role.USER.name())
                                .requestMatchers("/transfers").hasAuthority(Role.USER.name())
                                .requestMatchers("/useraccounts").hasAuthority(Role.USER.name())
                                .requestMatchers("/addconnection").hasAuthority(Role.USER.name())
                                .anyRequest().authenticated()

                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/transfers", true)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
