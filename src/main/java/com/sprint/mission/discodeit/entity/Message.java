package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message extends Entity implements Serializable {

    private String content;
    private UUID channelID;
    private UUID authorId;

    //생성자
    public Message(String content, UUID authorId, UUID channelID) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelID = channelID;
    }

    //getter setter

    @Override
    public String toString() {
        return "메세지 : " +
                "content='" + content + '\'' +
                ", channelID=" + channelID +
                ", authorId=" + authorId +
                "} " + super.toString();
    }


    public void updateContent(String content) {
        this.content = content;
    }

}
