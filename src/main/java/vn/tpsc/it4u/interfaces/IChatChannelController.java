package vn.tpsc.it4u.interfaces;

import vn.tpsc.it4u.DTOs.ChatChannelInitializationDTO;
import vn.tpsc.it4u.DTOs.ChatMessageDTO;
import vn.tpsc.it4u.exception.IsSameUserException;
import vn.tpsc.it4u.exception.UserNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IChatChannelController {
    ChatMessageDTO chatMessage(@DestinationVariable String channelId, ChatMessageDTO message)
        throws BeansException, UserNotFoundException;

    ResponseEntity<String> establishChatChannel(@RequestBody ChatChannelInitializationDTO chatChannelInitialization)
        throws IsSameUserException, UserNotFoundException; 

    ResponseEntity<String> getExistingChatMessages(@PathVariable("channelUuid") String channelUuid);
}
