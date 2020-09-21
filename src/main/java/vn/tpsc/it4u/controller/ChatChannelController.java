package vn.tpsc.it4u.controller;

import java.util.List;
import vn.tpsc.it4u.DTOs.ChatChannelInitializationDTO;
import vn.tpsc.it4u.DTOs.ChatMessageDTO;
import vn.tpsc.it4u.DTOs.EstablishedChatChannelDTO;
import vn.tpsc.it4u.interfaces.IChatChannelController;
import vn.tpsc.it4u.service.ChatService;
import vn.tpsc.it4u.shared.http.JSONResponseHelper;
import vn.tpsc.it4u.exception.IsSameUserException;
import vn.tpsc.it4u.exception.UserNotFoundException;
import vn.tpsc.it4u.model.User;
import vn.tpsc.it4u.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("${app.api.version}")
public class ChatChannelController implements IChatChannelController {
  @Autowired
  private ChatService chatService;

  @Autowired
    private UserService userService;

    @MessageMapping("/private.chat.{channelId}")
    @SendTo("/topic/private.chat.{channelId}")
    public ChatMessageDTO chatMessage(@DestinationVariable String channelId, ChatMessageDTO message)
        throws BeansException, UserNotFoundException {
      chatService.submitMessage(message);

      return message;
    }

    @RequestMapping(value="/private-chat/channel", method=RequestMethod.PUT, produces="application/json", consumes="application/json")
    public ResponseEntity<String> establishChatChannel(@RequestBody ChatChannelInitializationDTO chatChannelInitialization) 
        throws IsSameUserException, UserNotFoundException { 
      String channelUuid = chatService.establishChatSession(chatChannelInitialization);
      User userOne = userService.getUser(chatChannelInitialization.getUserIdOne());
      User userTwo = userService.getUser(chatChannelInitialization.getUserIdTwo());

      EstablishedChatChannelDTO establishedChatChannel = new EstablishedChatChannelDTO(
        channelUuid,
        userOne.getUsername(),
        userTwo.getUsername()
      );
    
      return JSONResponseHelper.createResponse(establishedChatChannel, HttpStatus.OK);
    }
    
    @RequestMapping(value="/private-chat/channel/{channelUuid}", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<String> getExistingChatMessages(@PathVariable("channelUuid") String channelUuid) {
      List<ChatMessageDTO> messages = chatService.getExistingChatMessages(channelUuid);

      return JSONResponseHelper.createResponse(messages, HttpStatus.OK);
    }
}