package com.nidhal.demo.Config;

import com.nidhal.demo.chat.ChatMessage;
import com.nidhal.demo.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketEventListener {
    private SimpMessageSendingOperations messageTemplate ;
    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username!= null){
            log.info("user déconnecté : {}",username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }

    }
}
