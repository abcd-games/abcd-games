package com.github.abcdgames.backend.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/users")
@Slf4j
public class AppUserController {

    public static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;
    private final AppUserService appUserService;

    @GetMapping(produces = CONTENT_TYPE_JSON, path = "/me")
    public AppUserResponse getMe() {
        return appUserService.getLoggedInUser();
    }

    @GetMapping(produces = CONTENT_TYPE_JSON)
    public List<AppUserResponse> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping(path = "/{id}", produces = CONTENT_TYPE_JSON)
    public AppUserResponse getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id);
    }

    @PostMapping(consumes = CONTENT_TYPE_JSON, produces = CONTENT_TYPE_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse createUser(@Validated @RequestBody AppUserRequest appUserRequest) {
        return appUserService.createUser(appUserRequest);
    }

    @PutMapping(path = "/{id}", consumes = CONTENT_TYPE_JSON, produces = CONTENT_TYPE_JSON)
    public AppUserResponse updateUser(@PathVariable Long id, @Validated @RequestBody AppUserRequest appUserRequest) {
        return appUserService.updateUser(id, appUserRequest);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        return appUserService.deleteUser(id);
    }

}
