package com.github.abcdgames.backend.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUsername(String username);
    Optional<AppUser> findAppUserByEmail(String email);
    boolean existsAppUserByUsername(String username);
    boolean existsAppUserByEmail(String email);
}
