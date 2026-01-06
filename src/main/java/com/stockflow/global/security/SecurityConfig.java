package com.stockflow.global.security;

import com.stockflow.global.security.filter.JwtAuthenticationFilter;
import com.stockflow.global.security.filter.OAuth2SuccessHandler;
import com.stockflow.global.security.jwt.JwtTokenProvider;
import com.stockflow.global.security.service.CustomUserDetailService;
import com.stockflow.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] allowUrl = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/login",
            "/signup",
            "/api/form",
            "/oauth2/authorization/kakao",
            "http://localhost:8080/oauth2/authorization/kakao"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider,
                                           CustomOAuth2UserService customOAuth2UserService,
                                           OAuth2SuccessHandler oAuth2SuccessHandler) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //HTTP Basic & Form Login 해체(Oauth2 로그인 사용)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(allowUrl).permitAll()
//                        .anyRequest().permitAll())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리 서비스
                        )
                        .successHandler(oAuth2SuccessHandler) // 로그인 성공 시 JWT 발급 핸들러
                )
                // JWT 필터를 시큐리티 체인에 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        ;

        return http.build();
    }

//    @Bean
//    public Filter jwtFilter() {
//        return new JwtFilter(jwtProvider, principalDetailsService);
//    }

//    @Bean
//    public PasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
