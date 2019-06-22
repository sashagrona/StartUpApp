package net.bigmir.services;

import net.bigmir.model.Chat;
import net.bigmir.model.ChatMessage;
import net.bigmir.model.SimpleUser;
import net.bigmir.repositories.ChatMessageRepository;
import net.bigmir.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.calendar.LocalGregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Transactional
    public Chat getChatByName(String name){
        return chatRepository.getChatByName(name);
    }

    @Transactional
    public boolean ifChatExists(Chat chat){
        return chatRepository.ifChatExists(chat.getChatName());
    }

    @Transactional
    public List<ChatMessage> getMessagesFromChat(Chat chat){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       List<ChatMessage> messages = chatMessageRepository.getMessagesFromChat(chat);
        for (ChatMessage m:messages) {
                Date date = m.getDate();
                String formatDate = sdf.format(date);
                m.setDateFormat(formatDate);
        }
        return messages;
    }

    @Transactional
    public Set<Chat> getMyChats(SimpleUser user){
        return chatRepository.getMyChats(user);
    }
}
