package com.happy.biling.domain.repository;

import com.happy.biling.domain.entity.UserMessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageReadStatusRepository extends JpaRepository<UserMessageReadStatus, Long> {

    @Query("SELECT SUM(u.unreadChatCount) FROM UserMessageReadStatus u WHERE u.user.id = :userId")
    Integer findTotalUnreadChatCountByUserId(Long userId);
}
