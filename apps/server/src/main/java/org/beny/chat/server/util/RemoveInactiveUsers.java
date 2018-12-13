package org.beny.chat.server.util;

import org.apache.log4j.LogManager;
import org.beny.chat.common.Config;
import org.beny.chat.server.ChatServer;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class RemoveInactiveUsers extends TimerTask {

    @Override
    public void run() {
        ChatServer.INSTANCE.getUsers().entrySet().removeIf(e -> e.getValue().getLastActivity().isBefore(LocalDateTime.now().minusSeconds(Config.MAX_INACTIVE_TIME_IN_SEC)));
        LogManager.getLogger(getClass()).info("Inactive users removed");
    }

}
