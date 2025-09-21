package configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails joey = User.builder()
                .username("joey")
                .password(passwordEncoder.encode("joey"))
                .roles("ADMIN")
                .build();

        UserDetails ross = User.builder()
                .username("ross")
                .password(passwordEncoder.encode("ross"))
                .roles("USER")
                .build();

        UserDetails rachel = User.builder()
                .username("rachel")
                .password(passwordEncoder.encode("rachel"))
                .roles("USER")
                .build();

        UserDetails chandler = User.builder()
                .username("chandler")
                .password(passwordEncoder.encode("chandler"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(joey,ross,rachel,chandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/wigellgym/listcanceled",
                                "/api/wigellgym/listupcoming",
                                "/api/wigellgym/listpast",
                                "/api/wigellgym/addworkout",
                                "/api/wigellgym/updateworkout",
                                "/api/wigellgym/remworkout/**",
                                "/api/wigellgym/addinstructor",
                                "api/wigellgym/instructors")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/wigellgym/workouts",
                                "/api/wigellgym/instructors",
                                "/api/wigellgym/bookworkout",
                                "/api/wigellgym/cancelworkout",
                                "/api/wigellgym/mybookings")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
