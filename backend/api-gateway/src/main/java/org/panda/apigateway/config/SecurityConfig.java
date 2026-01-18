package org.panda.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//
//        http
//                .csrf(s->s.disable())
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/auth/**").permitAll()
//                        .anyExchange().authenticated()
//                );
//
//
//        return http.build();
//    }
//}
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex.anyExchange().permitAll());
        return http.build();
    }
}

