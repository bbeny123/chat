package org.beny.chat.client.service;

import org.beny.chat.client.ChatClient;
import org.beny.chat.common.domain.Message;

import java.util.List;

import static org.beny.chat.client.ChatClient.*;

public class ClientService {

    public static Long login(String nick) throws Exception {
        Long id = ChatClient.getId();
        if (id != null) {
            ChatClient.logout();
            getService().logout(id);
        }
        id = getService().login(nick);
        ChatClient.login(id);
        return id;
    }

    public static boolean logout() throws Exception {
        Long id = ChatClient.getId();
        ChatClient.logout();
        return getService().logout(id);
    }

    public static boolean createChannel(String name) throws Exception {
        return getService().createChannel(getId(), name);
    }

    public static boolean joinChannel(String name) throws Exception {
        return getService().joinChannel(getId(), name);
    }

    public static boolean channelMessage(String message) throws Exception {
        return getService().channelMessage(getId(), message);
    }

    public static boolean privateMessage(String target, String message) throws Exception {
        return getService().privateMessage(getId(), target, message);
    }

    public static List<String> getChannels() throws Exception {
        return getService().getChannels(getId());
    }

    public static List<String> getChannelUsers() throws Exception {
        return getService().getChannelUsers(getId());
    }

    public static List<Message> getMessages() throws Exception {
        List<Message> messages = getService().getMessages(getId(), getSyncDate());
        syncDate();
        return messages;
    }

}