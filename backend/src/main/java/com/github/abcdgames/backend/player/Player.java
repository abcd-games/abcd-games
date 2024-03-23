package com.github.abcdgames.backend.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "player")
public class Player {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "display_name")
    private String displayName;

}
