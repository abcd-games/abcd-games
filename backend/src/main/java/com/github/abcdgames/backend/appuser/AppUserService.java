package com.github.abcdgames.backend.appuser;

import com.github.abcdgames.backend.utility.PasswordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    public static final String RETRIEVED_USER = "Retrieved user: {}";
    public static final String NOT_FOUND = " not found.";
    private final AppUserRepository appUserRepository;
    private final PasswordService passwordService;

    @PreAuthorize("hasRole('ADMIN')")
    List<AppUserResponse> getAllUsers() {
        return appUserRepository
                .findAll()
                .stream()
                .map(AppUserResponse::fromAppUser)
                .toList();
    }

    AppUserResponse getUserById(Long id) {
        AppUser appUser = findUserById(id);
        log.debug(RETRIEVED_USER, appUser.getId());
        return AppUserResponse.fromAppUser(appUser);
    }

    AppUserResponse updateUser(Long id, AppUserRequest appUserRequest) {
        AppUser appUserToUpdate = findUserById(id);

        appUserToUpdate.setPassword(passwordService.encodePassword(appUserRequest.password()));
        appUserToUpdate.setEmail(appUserRequest.email());
        appUserToUpdate.setUsername(appUserRequest.username());

        AppUser savedAppUser = appUserRepository.save(appUserToUpdate);

        log.debug("Updated user: {}", savedAppUser);
        return AppUserResponse.fromAppUser(savedAppUser);
    }

    AppUserResponse createUser(AppUserRequest appUserRequest) {
        if (checkIfAppUserExists(appUserRequest)) {
            throw new AppUserAlreadyExistsException("User with username " + appUserRequest.username() + " or email " + appUserRequest.email() + " already exists.");
        }

        AppUser savedAppUser = appUserRepository.save(AppUser.fromAppUserRequest(appUserRequest));
        log.debug("Created user: {}", savedAppUser);

        return AppUserResponse.fromAppUser(savedAppUser);
    }

    String deleteUser(Long id) {
        appUserRepository.deleteById(id);
        log.debug("Deleted user with id: {}", id);
        return "User with id " + id + " deleted.";
    }

    private boolean checkIfAppUserExists(AppUserRequest appUserRequest) {
        return existsAppUserByUsername(appUserRequest.username()) || existsAppUserByEmail(appUserRequest.email());
    }

    private AppUser findUserById(Long id) {
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + NOT_FOUND));
    }

    private boolean existsAppUserByUsername(String username) {
        return appUserRepository.existsAppUserByUsername(username);
    }

    private boolean existsAppUserByEmail(String email) {
        return appUserRepository.existsAppUserByEmail(email);
    }

    AppUserResponse getLoggedInUser() {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken token) {
            String provider = token.getAuthorizedClientRegistrationId();
            var principal = token.getPrincipal();
            if (provider.equals("google")) {
                String email = principal.getAttribute("email");
                AppUser appUser = appUserRepository.findAppUserByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException("User with email " + email + NOT_FOUND));
                log.debug(RETRIEVED_USER, appUser.getId());
                return AppUserResponse.fromAppUser(appUser);
            }
        } else {
            String username = authentication.getName();
            boolean containsEmail = username.contains("@");

            if (containsEmail) {
                AppUser appUser = appUserRepository.findAppUserByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("User with email " + username + NOT_FOUND));
                log.debug(RETRIEVED_USER, appUser.getId());
                return AppUserResponse.fromAppUser(appUser);
            }

            AppUser appUser = appUserRepository.findAppUserByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("User with username " + username + NOT_FOUND));
            log.debug(RETRIEVED_USER, appUser.getId());
            return AppUserResponse.fromAppUser(appUser);
        }
        throw new IllegalStateException("User not found.");
    }

    public void logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
    }
}
