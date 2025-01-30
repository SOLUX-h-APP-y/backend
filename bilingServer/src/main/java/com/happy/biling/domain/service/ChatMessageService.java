package com.happy.biling.domain.service;

import com.happy.biling.domain.entity.*;
import com.happy.biling.domain.entity.enums.PostStatus;
import com.happy.biling.domain.repository.*;
import com.happy.biling.dto.chat.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private UserMessageReadStatusRepository userMessageReadStatusRepository;

    @Transactional
    public List<ChatRoomResponse> getChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatRoom> chatRooms = chatRoomRepository.findByOwnerOrRenter(user, user);

        return chatRooms.stream().map(chatRoom -> {
            User otherUser = chatRoom.getOwner().equals(user) ? chatRoom.getRenter() : chatRoom.getOwner();
            ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomOrderByCreateAtDesc(chatRoom).orElse(null);

            if (lastMessage != null) {
                chatRoom.setLastMessageContent(lastMessage.getContent());
                chatRoom.setLastMessageTime(lastMessage.getCreateAt());
                chatRoomRepository.save(chatRoom);
            }

            return new ChatRoomResponse(
                    chatRoom.getId(),
                    chatRoom.getPost().getId(),
                    otherUser.getProfileImage(),
                    chatRoom.getPost().getTitle(),
                    lastMessage != null ? lastMessage.getContent() : "No messages",
                    chatRoom.getUnreadCount(),
                    lastMessage != null ? lastMessage.getCreateAt() : LocalDateTime.now()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public ChatRoomDetailResponse getChatRoomDetails(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        Post post = chatRoom.getPost();
        User otherUser = chatRoom.getRenter();

        String postImageUrl = postImageRepository.findTopByPostOrderByOrderSequenceAsc(post)
                .map(PostImage::getImageUrl)
                .orElse(null);

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderByCreateAtAsc(chatRoom);

        PostStatus postStatus = post.getStatus();

        return new ChatRoomDetailResponse(
                chatRoom.getId(),
                post.getTitle(),
                post.getLocationName(),
                postImageUrl,
                postStatus,
                otherUser.getProfileImage(),
                messages.stream().map(message -> new MessageResponse(
                        message.getSender().getNickname(),
                        message.getSender().getId(),
                        message.getContent(),
                        message.getCreateAt()
                )).collect(Collectors.toList())
        );
    }

    @Transactional
    public SendMessageResponse sendMessage(SendMessageRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        User renter = userRepository.findById(request.getRenterId())
                .orElseThrow(() -> new RuntimeException("Renter not found"));

        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseGet(() -> {
                    ChatRoom newChatRoom = new ChatRoom();
                    newChatRoom.setPost(post);
                    newChatRoom.setOwner(owner);
                    newChatRoom.setRenter(renter);
                    return chatRoomRepository.save(newChatRoom);
                });

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(owner);
        chatMessage.setContent(request.getContent());
        chatMessage.setIsRead(false);
        chatMessageRepository.save(chatMessage);

        User receiver = owner.equals(chatMessage.getSender()) ? renter : owner;
        UserMessageReadStatus readStatus = userMessageReadStatusRepository.findByUserAndChatRoom(receiver, chatRoom)
                .orElseGet(() -> {
                    UserMessageReadStatus newStatus = new UserMessageReadStatus();
                    newStatus.setUser(receiver);
                    newStatus.setChatRoom(chatRoom);
                    newStatus.setUnreadChatCount(0);
                    return newStatus;
                });

        readStatus.setUnreadChatCount(readStatus.getUnreadChatCount() + 1);
        readStatus.setUpdateAt(LocalDateTime.now());
        userMessageReadStatusRepository.save(readStatus);

        return new SendMessageResponse(chatRoom.getId(), "Message sent successfully");
    }

    @Transactional
    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        UserMessageReadStatus readStatus = userMessageReadStatusRepository
                .findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new RuntimeException("No unread messages"));

        readStatus.setUnreadChatCount(0);
        readStatus.setUpdateAt(LocalDateTime.now());
        userMessageReadStatusRepository.save(readStatus);
    }
}
