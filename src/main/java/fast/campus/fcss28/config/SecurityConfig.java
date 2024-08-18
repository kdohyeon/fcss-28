package fast.campus.fcss28.config;

import fast.campus.fcss28.filter.CustomSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(a -> a.anyRequest().permitAll());
//        httpSecurity.authorizeHttpRequests(a -> a.anyRequest().authenticated());
//        httpSecurity.addFilterAfter(new CustomSecurityFilter(), BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
