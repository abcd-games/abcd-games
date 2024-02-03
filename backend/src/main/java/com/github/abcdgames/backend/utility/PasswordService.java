package com.github.abcdgames.backend.utility;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    public String encodePassword(String password) {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(password);
    }
}
