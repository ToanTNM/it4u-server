package vn.tpsc.it4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.tpsc.it4u.model.MessageBean;
import vn.tpsc.it4u.model.MessageBean;

@Controller
public class SocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/user-all")
    @SendTo("/topic/user")
    public MessageBean send(@Payload MessageBean message) {
        return message;
    }
    
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, @Payload MessageBean message) {
        System.out.println("handling send message: " + message + " to: " + to);
        simpMessagingTemplate.convertAndSend("/topic/user/" + to, message);
    }
}