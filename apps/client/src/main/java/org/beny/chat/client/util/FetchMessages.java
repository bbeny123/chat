package org.beny.chat.client.util;

import org.beny.chat.client.ChatClient;
import org.beny.chat.client.service.ClientService;

import java.util.TimerTask;

public class FetchMessages extends TimerTask {

    @Override
    public void run() {
        if (ChatClient.getId() != null) {
            try {
                ClientService.getMessages().forEach(System.out::println);
            } catch (Exception ex) {}
        }
    }

}
