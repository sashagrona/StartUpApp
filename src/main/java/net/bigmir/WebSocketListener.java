package net.bigmir;

import net.bigmir.dto.ChatMessageDTO;
import net.bigmir.model.TypeMessage;
import net.bigmir.services.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
//Listener which logs connecting and disconnecting users
public class WebSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private SimpleUserService simpleUserService;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Registered a new connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userEmail = (String) headerAccessor.getSessionAttributes().get("userEmail");
        String chatName = (String) headerAccessor.getSessionAttributes().get("chatName");
        if(userEmail != null) {
            logger.info("User Disconnected : " + simpleUserService.findByEmail(userEmail).getLogin());
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
            chatMessageDTO.setType(TypeMessage.OFFLINE);
            chatMessageDTO.setSender(simpleUserService.findByEmail(userEmail).toDTO());
            chatMessageDTO.setChat(chatName);
            messagingTemplate.convertAndSend("/topic/private." + chatName, chatMessageDTO);
        }
    }
}
