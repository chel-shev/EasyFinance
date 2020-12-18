package ru.ixec.easyfinance.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.ixec.easyfinance.security.JwtConfigurer;
import ru.ixec.easyfinance.security.JwtRequestFilter;
import ru.ixec.easyfinance.security.service.JwtClientDetailsService;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    private final JwtClientDetailsService jwtClientDetailsService;


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/client/signup");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtRequestFilter));
    }

//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*")
//                .allowedMethods("HEAD", "GET", "PUT", "POST",
//                        "DELETE", "PATCH").allowedHeaders("*");
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(jwtClientDetailsService);
//    }
}