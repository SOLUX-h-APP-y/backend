package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.ChatMessage;
import com.happy.biling.domain.entity.ChatRoom;
import com.happy.biling.domain.entity.Post;
import com.happy.biling.domain.entity.User;
import com.happy.biling.domain.repository.ChatMessageRepository;
import com.happy.biling.domain.repository.ChatRoomRepository;
import com.happy.biling.domain.repository.PostRepository;
import com.happy.biling.domain.repository.UserRepository;
import com.happy.biling.dto.chat.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendMessage(SendMessageRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (request.getOwnerId() == null || request.getRenterId() == null) {
            throw new IllegalArgumentException("Owner ID and Renter ID must not be null");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        User renter = userRepository.findById(request.getRenterId())
                .orElseThrow(() -> new RuntimeException("Renter not found"));

        ChatRoom chatRoom = chatRoomRepository.findByPostAndOwnerAndRenter(post, owner, renter);

        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setPost(post);
            chatRoom.setOwner(owner);
            chatRoom.setRenter(renter);
            chatRoom = chatRoomRepository.save(chatRoom);
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(owner);
        chatMessage.setContent(request.getContent());
        chatMessage.setIsRead(false);

        chatMessageRepository.save(chatMessage);
    }
}
