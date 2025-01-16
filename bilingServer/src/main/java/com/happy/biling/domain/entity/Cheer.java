package com.happy.biling.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cheers")
public class Cheer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Setter
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "total_cheers")
    private Integer totalCheers = 0;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

}
