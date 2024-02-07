package com.github.abcdgames.backend.appuser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/users")
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/me")
    public AppUserResponse getMe() {
        return appUserService.getLoggedInUser();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<AppUserResponse> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public AppUserResponse getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id);
    }

    @PostMapping(path = "/login", produces = APPLICATION_JSON_VALUE)
    public AppUserResponse login() {
        return appUserService.getLoggedInUser();
    }

    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        appUserService.logout(session);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse createUser(@Validated @RequestBody AppUserRequest appUserRequest) {
        return appUserService.createUser(appUserRequest);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public AppUserResponse updateUser(@PathVariable Long id, @Validated @RequestBody AppUserRequest appUserRequest) {
        return appUserService.updateUser(id, appUserRequest);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        return appUserService.deleteUser(id);
    }

}
