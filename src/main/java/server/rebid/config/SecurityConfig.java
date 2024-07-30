package server.rebid.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import server.rebid.auth.security.filter.JwtAuthFilter;
import server.rebid.auth.security.oauth.handler.CustomSuccessHandler;
import server.rebid.auth.security.oauth.service.CustomOAuth2UserService;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity
//@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Value("${frontend.base_url}")
    private String frontendBaseUrl;

    @Value("${backend.base_url")
    private String backendBaseUrl;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/public/**"),
                new AntPathRequestMatcher("/favicon.ico"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/health"),
                new AntPathRequestMatcher("/hello"),
                new AntPathRequestMatcher("/error")
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList(frontendBaseUrl, backendBaseUrl));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
    }

    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers(auth -> auth
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**"
                        )
                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
//                .securityMatchers(auth -> auth
//                        .requestMatchers(
//                                "/member",
//                                ""
//                        )
//                )
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth2/authorization/**",
                                "/login/oauth2/code/**",
                                "/health", "/hello", "/bids", "/bids/{bidId}", "/bids/real-time", "/bids/imminent", "/bids/category"
                                ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
