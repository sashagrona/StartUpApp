package net.bigmir.controllers;

import com.google.gson.Gson;
import net.bigmir.dto.ChatMessageDTO;
import net.bigmir.model.Chat;
import net.bigmir.model.ChatMessage;
import net.bigmir.model.SimpleUser;
import net.bigmir.services.ChatMessageService;
import net.bigmir.services.ChatService;
import net.bigmir.services.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class ChatController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;


    @RequestMapping("/chat/all")
    public String showAll(Authentication authentication, Model model){

        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        Set<Chat> chats = chatService.getMyChats(user);
        model.addAttribute("user",user);
        if(!chats.isEmpty()) {
            model.addAttribute("chats", chats);
        }
        return "all_chats";

    }

    @RequestMapping("/chat/{name}")
    public String showChat(@PathVariable("name") String startUpName, Authentication authentication, Model model) {

        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        Chat chat = chatService.getChatByName(startUpName);
        List<ChatMessage> messages = chatService.getMessagesFromChat(chat);
        List<ChatMessageDTO> messageDTOS = new LinkedList<>();
        messages.forEach(m -> messageDTOS.add(m.toDTO()));
        if ((chatService.ifChatExists(chat))&&(simpleUserService.ifUserBelongsChat(chat,user))) {
            model.addAttribute("user", user.toDTO());
            model.addAttribute("startUpName", startUpName);
            model.addAttribute("messages", messageDTOS);
            return "chat";
        } else {
            return "redirect:/forbidden";
        }
    }

    @MessageMapping("{name}/chat.send")
    @SendTo("/topic/private.{name}")
    public ChatMessageDTO sendMessage(@PathVariable("name") String chatName, @Payload ChatMessageDTO chatMessageDTO) {
//        obtaining message and saving in DB
        SimpleUser user = simpleUserService.findByEmail(chatMessageDTO.getSender().getEmail());
        Chat chat = chatService.getChatByName(chatMessageDTO.getChat());
        ChatMessage chatMessage = new ChatMessage(chatMessageDTO.getType(),chatMessageDTO.getContent(),user,chat, chatMessageDTO.getDate());
        System.out.println(chatMessage.getDate());
        chatMessageService.saveMessage(chatMessage);
        return chatMessageDTO;
    }

    @MessageMapping("{name}/chat.register")
    @SendTo("/topic/private.{name}")
    public ChatMessageDTO registerUser(@PathVariable("name") String chatJson, @Payload ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        Gson gson = new Gson();
        String chatName = gson.fromJson(chatJson, ChatMessageDTO.class).getChat();
        System.out.println(chatName);

        headerAccessor.getSessionAttributes().put("chatName", chatName);
        headerAccessor.getSessionAttributes().put("userEmail", gson.fromJson(chatJson, ChatMessageDTO.class).getSender().getEmail());

        return chatMessageDTO;
    }
}
