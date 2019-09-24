package com.radeckiy.chat.repositories;

import com.radeckiy.chat.models.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    Message findBy_id(ObjectId _id);
}
