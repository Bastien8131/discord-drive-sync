package fr.bastienbories.discorddrivesync.core.controller;

import fr.bastienbories.discorddrivesync.core.exception.MessageNotFoundException;
import fr.bastienbories.discorddrivesync.core.mapper.CoreMessageMapper;
import fr.bastienbories.discorddrivesync.core.model.CoreMessage;
import fr.bastienbories.discorddrivesync.core.model.dto.CoreMessageDto;
import fr.bastienbories.discorddrivesync.core.services.CoreMessageServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class CoreMessageController {

    private final CoreMessageServices coreMessageServices;
    private final CoreMessageMapper coreMessageMapper;

    public CoreMessageController(CoreMessageServices coreMessageServices, CoreMessageMapper coreMessageMapper) {
        this.coreMessageServices = coreMessageServices;
        this.coreMessageMapper = coreMessageMapper;
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public CoreMessageDto getMessageById(@PathVariable long id) {
        CoreMessage coreMessage = coreMessageServices.getById(id).orElseThrow(() -> new MessageNotFoundException(id));
        CoreMessageDto rs = coreMessageMapper.coreMessageToCoreMessageDto(coreMessage);
        return rs;
    }
}
