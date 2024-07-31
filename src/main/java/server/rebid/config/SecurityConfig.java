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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import server.rebid.auth.jwt.JwtAccessDeniedHandler;
import server.rebid.auth.jwt.JwtAuthenticationEntryPoint;
import server.rebid.auth.jwt.JwtAuthenticationExceptionHandler;
import server.rebid.auth.jwt.TokenProvider;
import server.rebid.auth.security.filter.JwtFilter;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
//@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig{

//    private final CustomOAuth2UserService customOAuth2UserService;
//    private final CustomSuccessHandler customSuccessHandler;
//    private final JwtAuthFilter jwtAuthFilter;

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
                new AntPathRequestMatcher("/imageTest"),
                new AntPathRequestMatcher("/hello"),
                new AntPathRequestMatcher("/error"),
                new AntPathRequestMatcher("/bids/"),
                new AntPathRequestMatcher("/bids/{bidid}/histories"),
                new AntPathRequestMatcher("/bids/imminent"),
                new AntPathRequestMatcher("/bids/category")

        );
    }

    public CorsConfigurationSource corsConfiguration() {
        return request -> {
            org.springframework.web.cors.CorsConfiguration config =
                    new org.springframework.web.cors.CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
            config.setAllowedMethods(Collections.singletonList("*")); // 모든 메소드 허용
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 모든 Origin 허용
            config.setAllowCredentials(true);   // 인증정보 허용
            return config;
        };
    }

//    @Bean
//    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatchers(auth -> auth
//                        .requestMatchers(
//                                "/oauth2/authorization/**",
//                                "/login/oauth2/code/**"
//                        )
//                )
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfiguration()))
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .oauth2Login((oauth2) -> oauth2
//                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                                .userService(customOAuth2UserService))
//                        .successHandler(customSuccessHandler))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
////                .securityMatchers(auth -> auth
////                        .requestMatchers(
////                                "/member",
////                                ""
////                        )
////                )
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfiguration()))
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/oauth2/authorization/**",
//                                "/login/oauth2/code/**",
//                                "/health", "/hello", "/bids", "/bids/{bidId}", "/bids/real-time", "/bids/imminent", "/bids/category",
//                                "/bids/{bidId}/histories", "/imageTest", "/members/**"
//                                ).permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler = new JwtAccessDeniedHandler();

    private final TokenProvider tokenProvider;

    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler =
            new JwtAuthenticationExceptionHandler();

    private static final String[] JWT_WHITE_LIST ={
            "users/email/code/**", "/users/register","/users/login","/users/reissue"
    };

    /**
     * 특정 경로에 대한 보안 설정을 무시하도록 설정
     * @return WebSecurityCustomizer
     */


    @Bean
    public SecurityFilterChain JwtFilterChain(HttpSecurity http) throws Exception {
        return http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfiguration()))
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // 비활성화
                .sessionManagement(
                        manage ->
                                manage.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)) // Session 사용 안함
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> {
//                            authorize.requestMatchers("/swagger-ui/**").permitAll();
                            authorize.requestMatchers("/members/signup").permitAll();
                            authorize.requestMatchers("/members/login").permitAll();
                            authorize.anyRequest().authenticated();
                        })
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(
                        new JwtFilter(tokenProvider, JWT_WHITE_LIST),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationExceptionHandler, JwtFilter.class)
                .build();
    }

}
