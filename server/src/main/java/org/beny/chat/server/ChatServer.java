package org.beny.chat.server;

import org.beny.chat.common.domain.Channel;
import org.beny.chat.common.domain.User;

import java.util.HashMap;
import java.util.Map;

public enum ChatServer {
    INSTANCE;

    private final Map<String, Channel> channels = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();

    public Map<String, Channel> getChannels() {
        return channels;
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
