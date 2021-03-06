package com.SafeWebDev.attempt.Controllers.Security;

import com.SafeWebDev.attempt.Models.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorized;


    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        //Setting permissions for every url
        http.authorizeRequests()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/res/**").permitAll()
                .antMatchers("/register_form").permitAll()
                .antMatchers("/coupon_form").hasRole("ADMIN")
                .antMatchers("/item_form").authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/items").permitAll()
                .antMatchers("/item/new").authenticated()
                .antMatchers("/item/edit/**").authenticated()
                .antMatchers("/editting/**").authenticated()
                .antMatchers("/item/**").permitAll()
                .antMatchers("/usr").authenticated()
                .antMatchers("/item/del/**").authenticated()
                .antMatchers("/cart").authenticated()
                .antMatchers("/cart/**").authenticated()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").authenticated()
                .antMatchers("/account/created").permitAll()
                .antMatchers("/comments").permitAll()
                .antMatchers("/NewComment").authenticated()
                .antMatchers("/createComment").authenticated()
                .antMatchers("/payments").authenticated()
                .antMatchers("/pay").authenticated()
                .antMatchers("/price/final").authenticated()
                .antMatchers("/coupons").permitAll()
                .antMatchers("/search").permitAll()
                .antMatchers("/item_form").authenticated()
                .antMatchers("/coupon_form").hasRole("ADMIN")
                .antMatchers("/register_form").permitAll()
                .antMatchers("/adminpage").hasRole("ADMIN")
                .antMatchers("/user/del/**").hasRole("ADMIN");

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //Setting the rest of the pages as authenticated
        http.authorizeRequests().anyRequest().authenticated();

        //Setting the login page variables
        http.formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/usr").failureUrl("/login");

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");



    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


}
