package com.radeckiy.chat.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    @Id
    private ObjectId _id;
    private MessageType type;
    private String content, sender, createDate;

    public Message() {
        _id = ObjectId.get();
        createDate = new SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss").format(new Date());
    }

    public Message(MessageType type, String content, String sender) {
        _id = ObjectId.get();
        this.type = type;
        this.content = content;
        this.sender = sender;
        createDate = new SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss").format(new Date());
    }

    public String get_id() {
        return _id.toHexString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        if(type == MessageType.JOIN)
            return createDate + " " + sender + " присоединился к чату!";
        else if(type == MessageType.LEAVE)
            return createDate + " " + sender + " покинул чат!";

        return createDate + " " + sender + " >> " + content;
    }
}
