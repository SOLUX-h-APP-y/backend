package com.happy.biling.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false)
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public boolean isRead() {
        return isRead != null && isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }
}
