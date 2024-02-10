package com.github.abcdgames.backend.appuser;

import jakarta.persistence.*;
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
    @Column(name = "username")
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private AppUserRole role;

}
