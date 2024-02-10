package com.github.abcdgames.backend.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserRepository userRepository;

    @GetMapping("/me")
    public AppUser getMe(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            return userRepository
                    .findAppUserByEmail(principal.getEmail())
                    .orElseGet(() -> userRepository.save(
                            AppUser
                                    .builder()
                                    .username(principal.getNickName())
                                    .email(principal.getEmail())
                                    .avatarUrl(principal.getPicture())
                                    .role(AppUserRole.USER)
                                    .build())
                    );
        }
        throw new OAuth2AuthorizationException(new OAuth2Error("invalid_token", "The token is invalid", null));
    }
}
