package vn.tpsc.it4u.service;

import vn.tpsc.it4u.DTOs.ChatChannelInitializationDTO;
import vn.tpsc.it4u.DTOs.ChatMessageDTO;
import vn.tpsc.it4u.interfaces.IChatService;
import vn.tpsc.it4u.mappers.ChatMessageMapper;
import vn.tpsc.it4u.model.ChatChannel;
import vn.tpsc.it4u.model.ChatMessage;
import vn.tpsc.it4u.repository.ChatChannelRepository;
import vn.tpsc.it4u.repository.ChatMessageRepository;
import vn.tpsc.it4u.exception.IsSameUserException;
import vn.tpsc.it4u.exception.UserNotFoundException;
import vn.tpsc.it4u.model.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import java.util.List;

@Service
public class ChatService implements IChatService {
  private ChatChannelRepository chatChannelRepository;

  private ChatMessageRepository chatMessageRepository;

  private UserService userService;

  @Autowired
  public ChatService(
      ChatChannelRepository chatChannelRepository,
      ChatMessageRepository chatMessageRepository,
      UserService userService) {
    this.chatChannelRepository = chatChannelRepository;
    this.chatMessageRepository = chatMessageRepository;
    this.userService = userService;
  }

  private String getExistingChannel(ChatChannelInitializationDTO chatChannelInitializationDTO) {
    List<ChatChannel> channel = chatChannelRepository
      .findExistingChannel(
        chatChannelInitializationDTO.getUserIdOne(),
        chatChannelInitializationDTO.getUserIdTwo()
      );
    
    return (channel != null && !channel.isEmpty()) ? channel.get(0).getUuid() : null;
  }

  private String newChatSession(ChatChannelInitializationDTO chatChannelInitializationDTO)
      throws BeansException, UserNotFoundException {
    ChatChannel channel = new ChatChannel(
      userService.getUser(chatChannelInitializationDTO.getUserIdOne()),
      userService.getUser(chatChannelInitializationDTO.getUserIdTwo())
    );
    
    chatChannelRepository.save(channel);

    return channel.getUuid();
  }

  public String establishChatSession(ChatChannelInitializationDTO chatChannelInitializationDTO)
      throws IsSameUserException, BeansException, UserNotFoundException {
    if (chatChannelInitializationDTO.getUserIdOne() == chatChannelInitializationDTO.getUserIdTwo()) {
      throw new IsSameUserException();
    }

    String uuid = getExistingChannel(chatChannelInitializationDTO);

    // If channel doesn't already exist, create a new one
    return (uuid != null) ? uuid : newChatSession(chatChannelInitializationDTO);
  }
  
  public void submitMessage(ChatMessageDTO chatMessageDTO)
      throws BeansException, UserNotFoundException {
    ChatMessage chatMessage = ChatMessageMapper.mapChatDTOtoMessage(chatMessageDTO);

    chatMessageRepository.save(chatMessage);

    User fromUser = userService.getUser(chatMessage.getAuthorUser().getId());
    User recipientUser = userService.getUser(chatMessage.getRecipientUser().getId());
  }
 
  public List<ChatMessageDTO> getExistingChatMessages(String channelUuid) {
    ChatChannel channel = chatChannelRepository.getChannelDetails(channelUuid);
    List<ChatMessage> chatMessages = 
      chatMessageRepository.getExistingChatMessages(
        channel.getUserOne().getId(),
        channel.getUserTwo().getId()
      );

    // TODO: fix this
    List<ChatMessage> messagesByLatest = Lists.reverse(chatMessages); 

    return ChatMessageMapper.mapMessagesToChatDTOs(messagesByLatest);
  }
}