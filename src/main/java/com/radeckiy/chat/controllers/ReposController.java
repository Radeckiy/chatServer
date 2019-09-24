package com.radeckiy.chat.controllers;

import com.radeckiy.chat.models.Message;
import com.radeckiy.chat.repositories.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReposController {
    private MessageRepository repository;

    public ReposController(MessageRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/messages", method = RequestMethod.GET)
    public List<Message> getMessages(@RequestParam(required = false) String sender,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) ObjectId id) {

        if(id != null)
            return Collections.singletonList(repository.findBy_id(id));

        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, 10, Sort.Direction.DESC, "createDate");

        if(sender != null && !sender.isEmpty())
            return repository.findAll(pageRequest).stream().filter(m -> m.getSender().equalsIgnoreCase(sender)).collect(Collectors.toList());

        return repository.findAll(pageRequest).getContent();
    }
}
