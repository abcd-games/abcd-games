package com.github.abcdgames.backend.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUserByUsername = appUserRepository.findAppUserByUsername(username);
        Optional<AppUser> appUserByEmail = appUserRepository.findAppUserByEmail(username);

        if (appUserByUsername.isPresent()) {
            log.debug("Retrieved user: {}", appUserByUsername.get());
            return appUserByUsername.get();
        } else if (appUserByEmail.isPresent()) {
            log.debug("Retrieved user: {}", appUserByEmail.get());
            return appUserByEmail.get();
        } else {
            log.error("User not found.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }
}
