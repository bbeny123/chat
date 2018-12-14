package org.beny.chat.server;

import org.beny.chat.common.Config;
import org.beny.chat.common.domain.Channel;
import org.beny.chat.common.domain.User;
import org.beny.chat.server.util.RemoveInactiveUsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public enum ChatServer {
    INSTANCE;

    private final Map<String, Channel> channels = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();

    ChatServer() {
        new Timer().schedule(new RemoveInactiveUsers(), Config.REMOVE_INACTIVE_TASK_PERIOD_IN_MS, Config.REMOVE_INACTIVE_TASK_PERIOD_IN_MS);
    }

    public static Map<String, Channel> getChannels() {
        return INSTANCE.channels;
    }

    public static Map<Long, User> getUsers() {
        return INSTANCE.users;
    }
}
