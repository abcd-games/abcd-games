package com.github.abcdgames.backend.appuser;

import com.github.abcdgames.backend.utility.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    public static final String RETRIEVED_USER = "Retrieved user: {}";
    private final AppUserRepository appUserRepository;
    private final PasswordService passwordService;

    List<AppUserResponse> getAllUsers() {
        var loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof AppUser appUser && (appUser.isAdmin())) {
            return appUserRepository
                    .findAll()
                    .stream()
                    .map(currentUser -> {
                        log.debug(RETRIEVED_USER, currentUser);
                        return AppUserResponse.fromAppUser(currentUser);
                    })
                    .toList();

        }
        throw new IllegalStateException("You can only retrieve all users if you are an admin.");
    }

    AppUserResponse getUserById(Long id) {
        var loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof AppUser appUser && appUser.getId().equals(id)) {
            AppUser retrievedUser = findUserById(id);
            log.debug(RETRIEVED_USER, retrievedUser);
            return AppUserResponse.fromAppUser(retrievedUser);
        }
        throw new IllegalStateException("You can only retrieve your own user.");
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
        throw new IllegalStateException("You can only update your own user.");
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
        throw new IllegalStateException("You can only delete your own user or if you are an admin.");
    }

    private boolean checkIfAppUserExists(AppUserRequest appUserRequest) {
        return existsAppUserByUsername(appUserRequest.username()) || existsAppUserByEmail(appUserRequest.email());
    }

    private AppUser findUserById(Long id) {
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("User with id " + id + " not found."));
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
            log.debug(RETRIEVED_USER, appUser);
            return AppUserResponse.fromAppUser(appUser);
        }
        throw new IllegalStateException("User not found.");
    }
}
