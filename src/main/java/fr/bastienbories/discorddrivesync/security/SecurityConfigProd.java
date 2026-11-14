package fr.bastienbories.discorddrivesync.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import fr.bastienbories.discorddrivesync.common.PublicRoute;
import jakarta.servlet.DispatcherType;

@Configuration
@Profile("prod")
public class SecurityConfigProd {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Error dispatches (e.g. forward to /error after a ResponseStatusException) must stay
                        // reachable, otherwise anonymous users get 401 instead of the real status code.
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/api/public/**", PublicRoute.Paths.PUBLIC_PREFIX + "/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()) //acceptable as a temporary configuration, but the Angular front end will need to be replaced with JWT authentication.
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
