package org.beny.chat.common.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;
    private String nickname;
    private LocalDateTime lastActivity;
    private Channel channel;
    private List<Message> privateMessages;

    public User(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.lastActivity = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Message> getPrivateMessages() {
        if (privateMessages == null) {
            privateMessages = new ArrayList<>();
        }
        return privateMessages;
    }
}
