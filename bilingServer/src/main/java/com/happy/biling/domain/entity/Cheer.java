package com.happy.biling.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cheers", indexes = {
        @Index(name = "idx_receiver_id", columnList = "receiver_id")
})
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

    @Setter
    @Column(name = "total_cheers")
    private Integer totalCheers = 0;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();
}
