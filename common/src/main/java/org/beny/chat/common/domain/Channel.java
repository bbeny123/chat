package org.beny.chat.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private String name;
    private List<User> users;
    private List<Message> channelMessages;

    public Channel(String name, User user) {
        this.name = name;
        this.getUsers().add(user);
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public List<Message> getChannelMessages() {
        if (channelMessages == null) {
            channelMessages = new ArrayList<>();
        }
        return channelMessages;
    }


}
