package vn.tpsc.it4u.mappers;

import java.util.ArrayList;
import java.util.List;
import vn.tpsc.it4u.DTOs.ChatMessageDTO;
import vn.tpsc.it4u.models.ChatMessage;
import vn.tpsc.it4u.models.User;

public class ChatMessageMapper {
	public static List<ChatMessageDTO> mapMessagesToChatDTOs(List<ChatMessage> chatMessages) {
		List<ChatMessageDTO> dtos = new ArrayList<ChatMessageDTO>();

		for (ChatMessage chatMessage : chatMessages) {
			dtos.add(
					new ChatMessageDTO(
							chatMessage.getContents(),
							chatMessage.getAuthorUser().getId(),
							chatMessage.getRecipientUser().getId(),
							chatMessage.getTimeSent()));
		}

		return dtos;
	}

	public static ChatMessage mapChatDTOtoMessage(ChatMessageDTO dto) {
		return new ChatMessage(

				// only need the id for mapping
				new User(dto.getFromUserId()),
				new User(dto.getToUserId()),

				dto.getContents());
	}
}
