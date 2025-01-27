package com.happy.biling.domain.service;

import com.happy.biling.domain.repository.UserMessageReadStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final UserMessageReadStatusRepository userMessageReadStatusRepository;

    public ChatService(UserMessageReadStatusRepository userMessageReadStatusRepository) {
        this.userMessageReadStatusRepository = userMessageReadStatusRepository;
    }

    @Transactional(readOnly = true)
    public Integer getTotalUnreadChatCount(Long userId) {
        return userMessageReadStatusRepository.findTotalUnreadChatCountByUserId(userId);
    }
}
