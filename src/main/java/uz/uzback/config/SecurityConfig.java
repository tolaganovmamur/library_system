package uz.uzback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/", "/api/auth/**","/api/booking/takeaway","/api/booking","/api/broughtBack/")
                .permitAll()
                .requestMatchers("/api/book/**").hasAnyRole("MODERATOR","ADMIN")
                .requestMatchers("/api/floor/**","/api/bookcase/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
                .exceptionHandling()
                .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
                .accessDeniedHandler(customBearerTokenAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}

