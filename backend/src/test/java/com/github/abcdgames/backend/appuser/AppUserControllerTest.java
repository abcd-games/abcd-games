package com.github.abcdgames.backend.appuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AppUserControllerTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void getMe_expectStatus200AndAppUserResponse_whenUserLoggedIn() throws Exception {
        AppUser adminUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);

        AppUserResponse appUserResponse = AppUserResponse.fromAppUser(adminUser);
        String appUserResponseJson = objectMapper.writeValueAsString(appUserResponse);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().json(appUserResponseJson));
    }

    @Test
    void getMe_expectStatus401_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsers_expectStatus200AndListOfAppUsers_whenLoggedInAndAdmin() throws Exception {
        AppUser adminUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllUsers_expectStatus403_whenLoggedInAndNotAdmin() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.USER);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null, loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsers_expectStatus401_whenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserById_expectStatus200AndMyOwnCredentials_whenLoggedIn() throws Exception {
        AppUser adminUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(adminUser);

        AppUserResponse appUserResponse = AppUserResponse.fromAppUser(adminUser);
        String appUserResponseJson = objectMapper.writeValueAsString(appUserResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(appUserResponseJson));
    }

    @Test
    void getUserById_expectStatus403_whenLoggedInAndTryingToAccessOtherCredentials() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(someUser);
        appUserRepository.save(loggedInUser);

        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createUser_expectStatus201AndSavedUser_whenUsernameAndEmailNotExist() throws Exception {
        AppUserRequest appUserRequest = new AppUserRequest("user", "user@user.de", "Password1234");
        String appUserRequestJson = objectMapper.writeValueAsString(appUserRequest);

        MvcResult createUserResult = mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(appUserRequestJson))
                .andExpect(status().isCreated())
                .andReturn();
        AppUserResponse appUserResponse = objectMapper.readValue(createUserResult.getResponse().getContentAsString(), AppUserResponse.class);

        assertNotNull(appUserResponse.id());
        assertEquals(appUserRequest.username(), appUserResponse.username());
        assertEquals(appUserRequest.email(), appUserResponse.email());
        assertEquals(AppUserRole.USER, appUserResponse.role());
    }

    @Test
    void createUser_expectStatus400_whenUsernameExist() throws Exception {
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        appUserRepository.save(someUser);
        AppUserRequest appUserRequest = new AppUserRequest("user2", "user@user.de", "Password1234");
        String appUserRequestJson = objectMapper.writeValueAsString(appUserRequest);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(appUserRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_expectStatus400_whenEmailExist() throws Exception {
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        appUserRepository.save(someUser);
        AppUserRequest appUserRequest = new AppUserRequest("user", "user2@user.de", "Password1234");
        String appUserRequestJson = objectMapper.writeValueAsString(appUserRequest);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(appUserRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_expectStatus200AndUpdatedUser_whenUserLoggedInAndTryingToEditOwnCredentials() throws Exception {
        AppUser adminUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(adminUser);

        AppUserRequest appUserRequest = new AppUserRequest("user2", "user@user.de", "Password1234");
        String appUserRequestJson = objectMapper.writeValueAsString(appUserRequest);

        MvcResult updateUserResult = mockMvc.perform(put("/api/users/1")
                        .contentType("application/json")
                        .content(appUserRequestJson))
                .andExpect(status().isOk())
                .andReturn();
        AppUserResponse appUserResponse = objectMapper.readValue(updateUserResult.getResponse().getContentAsString(), AppUserResponse.class);

        assertEquals(adminUser.getId(), appUserResponse.id());
        assertEquals(appUserRequest.username(), appUserResponse.username());
        assertEquals(appUserRequest.email(), appUserResponse.email());
        assertEquals(AppUserRole.ADMIN, appUserResponse.role());
    }

    @Test
    void updateUser_expectStatus403_whenUserLoggedInAndTryingToEditOtherCredentials() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(someUser);
        appUserRepository.save(loggedInUser);

        AppUserRequest appUserRequest = new AppUserRequest("user3", "user3@user.de", "Password1234");
        String appUserRequestJson = objectMapper.writeValueAsString(appUserRequest);

        mockMvc.perform(put("/api/users/2")
                        .contentType("application/json")
                        .content(appUserRequestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_expectStatus200_whenLoggedInAndUserWantsToDeleteItSelf() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(loggedInUser);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_expectStatus403_whenLoggedInAndUserWantsToDeleteOtherUser() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.USER);
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(someUser);
        appUserRepository.save(loggedInUser);

        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_expectStatus200_whenLoggedInAndAdminWantsToDeleteOtherUser() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        AppUser someUser = new AppUser(2L, "user2", "user2@user.de", "Password1234", AppUserRole.USER);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(someUser);
        appUserRepository.save(loggedInUser);

        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void logout_expectStatus204_whenLoggedIn() throws Exception {
        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    void logout_expectStatus401_whenNotLoggedIn() throws Exception {
        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_expectStatus200AndAppUserResponse_whenLoggedIn() throws Exception {
        AppUser loggedInUser = new AppUser(1L, "user1", "user@user.de", "Password1234", AppUserRole.ADMIN);
        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(loggedInUser, null,
                loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(principal);
        appUserRepository.save(loggedInUser);

        AppUserResponse appUserResponse = AppUserResponse.fromAppUser(loggedInUser);
        String appUserResponseJson = objectMapper.writeValueAsString(appUserResponse);

        mockMvc.perform(post("/api/users/login"))
                .andExpect(status().isOk())
                .andExpect(content().json(appUserResponseJson));
    }
}
