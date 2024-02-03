package com.github.abcdgames.backend.appuser;

public record AppUserResponse(
        Long id,
        String username,
        String email,
        AppUserRole appUserRole
) {
    static AppUserResponse fromAppUser(AppUser appUser) {
        return new AppUserResponse(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getEmail(),
                appUser.getAppUserRole()
        );
    }
}
