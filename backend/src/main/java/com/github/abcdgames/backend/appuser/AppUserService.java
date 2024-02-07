package com.github.abcdgames.backend.appuser;

import com.github.abcdgames.backend.utility.PasswordService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    public static final String RETRIEVED_USER = "Retrieved user: {}";
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
        var loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof AppUser appUser && appUser.getId().equals(id)) {
            AppUser retrievedUser = findUserById(id);
            log.debug(RETRIEVED_USER, retrievedUser.getId());
            return AppUserResponse.fromAppUser(retrievedUser);
        }
        throw new AccessDeniedException("You can only retrieve your own user.");
    }

    AppUserResponse updateUser(Long id, AppUserRequest appUserRequest) {
        var loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof AppUser appUser && appUser.getId().equals(id)) {
            AppUser appUserToUpdate = findUserById(id);

            appUserToUpdate.setPassword(passwordService.encodePassword(appUserRequest.password()));
            appUserToUpdate.setEmail(appUserRequest.email());
            appUserToUpdate.setUsername(appUserRequest.username());

            AppUser savedAppUser = appUserRepository.save(appUserToUpdate);

            log.debug("Updated user: {}", savedAppUser);
            return AppUserResponse.fromAppUser(savedAppUser);
        }
        throw new AccessDeniedException("You can only update your own user.");
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
        var loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof AppUser appUser && (appUser.getId().equals(id) || appUser.isAdmin())) {
            appUserRepository.deleteById(id);
            return "User with id " + id + " deleted.";
        }
        throw new AccessDeniedException("You can only delete your own user or if you are an admin.");
    }

    private boolean checkIfAppUserExists(AppUserRequest appUserRequest) {
        return existsAppUserByUsername(appUserRequest.username()) || existsAppUserByEmail(appUserRequest.email());
    }

    private AppUser findUserById(Long id) {
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found."));
    }

    private boolean existsAppUserByUsername(String username) {
        return appUserRepository.existsAppUserByUsername(username);
    }

    private boolean existsAppUserByEmail(String email) {
        return appUserRepository.existsAppUserByEmail(email);
    }

    AppUserResponse getLoggedInUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser appUser) {
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
