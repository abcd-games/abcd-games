package com.github.abcdgames.backend.config;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.appuser.AppUserRepository;
import com.github.abcdgames.backend.appuser.AppUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String API_USERS_ENDPOINT = "/api/users";
    public static final String ID = "/{id}";
    private final AppUserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(API_USERS_ENDPOINT + "/me").authenticated()
                        .requestMatchers(HttpMethod.GET, API_USERS_ENDPOINT).hasRole(AppUserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, API_USERS_ENDPOINT + ID).authenticated()
                        .requestMatchers(HttpMethod.POST, API_USERS_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.POST, API_USERS_ENDPOINT + "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, API_USERS_ENDPOINT + "/logout").authenticated()
                        .requestMatchers(HttpMethod.PUT, API_USERS_ENDPOINT + ID).authenticated()
                        .requestMatchers(HttpMethod.DELETE, API_USERS_ENDPOINT + ID).hasAnyRole(AppUserRole.ADMIN.name(),
                                AppUserRole.USER.name())
                        .anyRequest().hasRole(AppUserRole.ADMIN.name()))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .httpBasic(c -> c.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())))
                .oauth2Login(c -> c.defaultSuccessUrl(frontendUrl, true))
                .logout(c -> c.logoutSuccessUrl(frontendUrl));
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return request -> {
            OAuth2User user = delegate.loadUser(request);

            if (!userRepository.existsByGoogleId(user.getAttribute("sub"))) {
                AppUser newUser = new AppUser(
                        null,
                        user.getAttribute("email"),
                        user.getAttribute("email"),
                        passwordEncoder().encode(request.getAccessToken().getTokenValue()),
                        AppUserRole.USER,
                        user.getAttribute("sub")
                );
                userRepository.save(newUser);
            }

            return user;
        };
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser user = delegate.loadUser(userRequest);

            if (!userRepository.existsByGoogleId(user.getSubject())) {
                AppUser newUser = new AppUser(
                        null,
                        user.getEmail().substring(0, 20),
                        user.getEmail(),
                        passwordEncoder().encode(userRequest.getAccessToken().getTokenValue()),
                        AppUserRole.USER,
                        user.getSubject()
                );
                userRepository.save(newUser);
            }

            return user;
        };
    }

}
