package com.github.abcdgames.backend.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUserByUsername = appUserRepository.findAppUserByUsername(username);
        if (appUserByUsername.isPresent()) {
            log.debug("Retrieved user: {}", appUserByUsername.get());
            return new User(appUserByUsername.get().getUsername(), appUserByUsername.get().getPassword(),
                    List.of(new SimpleGrantedAuthority(appUserByUsername.get().getRole().name())));
        }

        Optional<AppUser> appUserByEmail = appUserRepository.findAppUserByEmail(username);
        if (appUserByEmail.isPresent()) {
            log.debug("Retrieved user: {}", appUserByEmail.get());
            return new User(appUserByEmail.get().getEmail(), appUserByEmail.get().getPassword(),
                    List.of(new SimpleGrantedAuthority(appUserByEmail.get().getRole().name())));
        }

        log.error("User not found.");
        throw new UsernameNotFoundException("User not found.");
    }
}
