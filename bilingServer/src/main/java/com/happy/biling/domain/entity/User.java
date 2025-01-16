package com.happy.biling.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.happy.biling.domain.entity.enums.Tier;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String kakaoId;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column
    private String profileImage;

    @Column
    private String locationName;

    @Column
    private Double locationLatitude;

    @Column
    private Double locationLongitude;

    @Column
    private String bio;

    @Column(nullable = false)
    private Integer rentalCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tier tier;

    @Column(nullable = false)
    private Boolean allowNotification = true;

    @Column(updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();
    
    @PrePersist
    private void prePersist() {
        if (tier == null) {
            tier = Tier.새싹;
        }
    }
}
