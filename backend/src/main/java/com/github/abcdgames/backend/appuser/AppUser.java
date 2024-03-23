package com.github.abcdgames.backend.appuser;

import com.github.abcdgames.backend.utility.PasswordService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppUser {
    @Id
    @Column(name = "app_user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "username")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @Column(unique = true, nullable = false, name = "email")
    @Email(message = "Invalid email.")
    private String email;

    @Column(nullable = false, name = "password")
    @Size(min = 8, max = 100)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private AppUserRole role;

    @Column(name = "google_id")
    private String googleId;

    static AppUser fromAppUserRequest(AppUserRequest appUserRequest) {
        PasswordService passwordService = new PasswordService();
        return AppUser.builder()
                .username(appUserRequest.username())
                .email(appUserRequest.email())
                .password(passwordService.encodePassword(appUserRequest.password()))
                .role(AppUserRole.USER)
                .build();
    }
}
