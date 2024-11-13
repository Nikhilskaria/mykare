package com.mykare.client_registeration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

////
@Configuration
@EnableWebSecurity
public class SecurityConfig  {
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.inMemoryAuthentication()
////                .withUser("user")
////                .password(passwordEncoder().encode("password"))
////                .roles("USER");
////    }
////
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .csrf().disable()
////                .authorizeRequests()
////                .antMatchers("/client/delete/**").authenticated()
////                .anyRequest().permitAll()
////                .and()
////                .httpBasic();
////    }
//
//
    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(customizer -> customizer.disable())// Disable CSRF for testing
//                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/rest/register").permitAll()
                        .requestMatchers("/rest/validate").permitAll()
                        .requestMatchers("/rest/delete").hasRole("ADMIN")
                        .requestMatchers("/rest/getClients").hasRole("ADMIN"))
//                .antMatchers("/public/**").permitAll()
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

        @Bean
                public UserDetailsService userDetailsService(){
            UserDetails admin = User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("password")
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(admin);
        }
    }

