package net.bigmir.services;

import net.bigmir.model.ChatMessage;
import net.bigmir.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Transactional
    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }



}
