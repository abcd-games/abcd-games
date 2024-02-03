package com.github.abcdgames.backend.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AppUserRequest(
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
        String username,

        @Email(message = "Invalid email.")
        String email,

        @Size(min = 8, max = 100)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number.")
        String password
) {
}
