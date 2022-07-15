package vn.tpsc.it4u.interfaces;

import java.util.List;
import vn.tpsc.it4u.DTOs.ChatChannelInitializationDTO;
import vn.tpsc.it4u.DTOs.ChatMessageDTO;
import vn.tpsc.it4u.exception.IsSameUserException;
import vn.tpsc.it4u.exception.UserNotFoundException;
import org.springframework.beans.BeansException;

public interface IChatService {
  String establishChatSession(ChatChannelInitializationDTO chatChannelInitializationDTO)
      throws IsSameUserException, BeansException, UserNotFoundException;

  void submitMessage(ChatMessageDTO chatMessageDTO)
      throws BeansException, UserNotFoundException;
  
  List<ChatMessageDTO> getExistingChatMessages(String channelUuid);
}