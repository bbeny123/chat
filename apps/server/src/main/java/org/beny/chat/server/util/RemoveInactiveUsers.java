package org.beny.chat.server.util;

import org.apache.log4j.LogManager;
import org.beny.chat.common.Config;
import org.beny.chat.common.domain.User;
import org.beny.chat.server.ChatServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class RemoveInactiveUsers extends TimerTask {

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        List<User> users = ChatServer.INSTANCE.getUsers().entrySet().stream().filter(e -> e.getValue().getLastActivity().isBefore(LocalDateTime.now().minusSeconds(Config.MAX_INACTIVE_TIME_IN_SEC))).map(Map.Entry::getValue).collect(Collectors.toList());

        users.forEach(u -> {
            if (u.getChannel() != null) {
                u.getChannel().getUsers().remove(u);
            }
            ChatServer.INSTANCE.getUsers().remove(u.getId());
            LogManager.getLogger(getClass()).info(String.format("Removed inactive user: %s (%d)", u.getNickname(), u.getId()));
        });

        LogManager.getLogger(getClass()).info(String.format("Removing inactive users ended after %d ms", System.currentTimeMillis() - start));
    }

}
