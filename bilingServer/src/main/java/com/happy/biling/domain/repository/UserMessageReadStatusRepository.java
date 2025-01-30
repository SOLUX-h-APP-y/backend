package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.UserMessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.entity.ChatRoom;
import java.util.Optional;

@Repository
public interface UserMessageReadStatusRepository extends JpaRepository<UserMessageReadStatus, Long> {

    Optional<UserMessageReadStatus> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    @Query("SELECT SUM(u.unreadChatCount) FROM UserMessageReadStatus u WHERE u.user.id = :userId")
    Integer findTotalUnreadChatCountByUserId(Long userId);
}
