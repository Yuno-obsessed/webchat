package sanity.nil.webchat.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.webchat.application.dto.*;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.infrastructure.db.impl.ChatFacade;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webchat")
@CrossOrigin
@RequiredArgsConstructor
public class Controller {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatFacade chatFacade;

    @PostMapping("/chat")
    public ResponseEntity<UUID> createChat(@RequestBody CreateChatDTO dto) {
        return ResponseEntity
                .status(201)
                .body(chatRepository.createChat(dto));
    }

    @PostMapping("/chat/join")
    public ResponseEntity<String> joinChat(
            @RequestBody JoinChatDTO dto
    ) {
        String response = chatFacade.joinChat(dto) ? "Success" : "Fail";
        return ResponseEntity
                .status(201)
                .body(response);
    }

    @GetMapping("/member/chat/{id}")
    public ResponseEntity<List<MemberChatsDTO>> getAllChatsByMember(
            @PathVariable("id") UUID memberID
    ) {
        return ResponseEntity
                .status(200)
                .body(chatRepository.getAllByMemberID(memberID));
    }

    @GetMapping("/chat/member/{id}")
    public ResponseEntity<List<ChatMemberDTO>> getChatMembers(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @PathVariable("id") UUID chatID
    ) {
        return ResponseEntity
                .status(200)
                .body(chatFacade.getChatMembers(chatID));
    }

    @PostMapping("/message/search")
    public ResponseEntity<PagedChatMessagesDTO> getMessagesByFilters(
            @RequestBody MessageFiltersDTO filters
    ) {
        return ResponseEntity
                .status(200)
                .body(chatFacade.getFilteredMessages(filters));
    }

    @PostMapping("/message")
    public ResponseEntity<UUID> sendMessage(
            @RequestBody OnSendMessageDTO messageDTO
    ) {
        return ResponseEntity
                .status(201)
                .body(chatFacade.sendMessage(messageDTO));
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken(
            @RequestParam(value = "type") String tokenType,
            @RequestParam(value = "channel", required = false) String channel
    ) {
        return ResponseEntity
                .status(200)
                .body(chatFacade.getToken(tokenType, channel));
    }
}
