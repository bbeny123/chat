package org.beny.chat.server.service;

import org.apache.xmlrpc.XmlRpcException;
import org.beny.chat.common.Config;
import org.beny.chat.common.domain.Channel;
import org.beny.chat.common.domain.Message;
import org.beny.chat.common.domain.User;
import org.beny.chat.server.ChatServer;
import org.beny.chat.server.exception.ChatErrors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ChatService {

    private final Map<String, Channel> channels = ChatServer.INSTANCE.getChannels();
    private final Map<Long, User> users = ChatServer.INSTANCE.getUsers();


    private User getUser(Long userId) throws XmlRpcException {
        User user = users.entrySet().stream().filter(e -> e.getKey().equals(userId)).findAny().orElseThrow(() -> new XmlRpcException(ChatErrors.INTERNAL_SERVER_CRITICAL_EXCEPTION.name())).getValue();
        user.setLastActivity(LocalDateTime.now());
        return user;
    }

    public Long login(String nickname) throws XmlRpcException {
        if (users.entrySet().stream().anyMatch(e -> e.getValue().getNickname().equals(nickname))) {
            throw new XmlRpcException(ChatErrors.NICKNAME_ALREADY_TAKEN.name());
        }

        for (int i = 0; i <= Config.MAX_GENERATION_ATTEMPTS; i++) {
            Long id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
            if (!users.containsKey(id)) {
                users.put(id, new User(id, nickname));
                return id;
            }
        }

        throw new XmlRpcException(ChatErrors.INTERNAL_SERVER_EXCEPTION.name());
    }

    public boolean logout(Long userId) throws XmlRpcException {
        users.remove(getUser(userId).getId());
        return true;
    }

    public boolean createChannel(Long userId, String channelName) throws XmlRpcException {
        if (channels.containsKey(channelName)) {
            throw new XmlRpcException(ChatErrors.CHANNEL_NAME_ALREADY_TAKEN.name());
        }

        User user = getUser(userId);

        Channel channel = new Channel(channelName, user);
        channels.put(channelName, channel);
        user.setChannel(channel);

        return true;
    }

    public boolean joinChannel(Long userId, String channelName) throws XmlRpcException {
        if (!channels.containsKey(channelName)) {
            throw new XmlRpcException(ChatErrors.CHANNEL_NOT_FOUND.name());
        }

        User user = getUser(userId);

        if (user.getChannel() != null && user.getChannel().getName().equals(channelName)) {
            throw new XmlRpcException(ChatErrors.ALREADY_ON_CHANNEL.name());
        }

        if (user.getChannel() != null) {
            channels.get(user.getChannel().getName()).getUsers().removeIf(u -> u.getId().equals(userId));
        }

        Channel channel = channels.get(channelName);
        user.setChannel(channel);
        channel.getUsers().add(user);

        return true;
    }

    public boolean channelMessage(Long userId, String message) throws XmlRpcException {
        User user = getUser(userId);
        if (user.getChannel() == null) {
            throw new XmlRpcException(ChatErrors.NOT_ON_CHANNEL.name());
        }

        user.getChannel().getChannelMessages().add(new Message(message, user.getNickname(), false));
        return true;
    }

    public boolean privateMessage(Long userId, String targetNickname, String message) throws XmlRpcException {
        User user = getUser(userId);
        User target = users.entrySet().stream().filter(e -> e.getValue().getNickname().equals(targetNickname)).findAny().orElseThrow(() -> new XmlRpcException(ChatErrors.USER_NOT_FOUND.name())).getValue();

        target.getPrivateMessages().add(new Message(message, user.getNickname(), true));
        return true;
    }

    public List<String> getChannels() {
        return channels.entrySet().stream().map(e -> e.getValue().getName()).collect(Collectors.toList());
    }

    public List<String> getChannelUsers(Long userId) throws XmlRpcException {
        return getUser(userId).getChannel().getUsers().stream().map(User::getNickname).collect(Collectors.toList());
    }

    public List<Message> getMessages(Long userId, LocalDateTime from) throws XmlRpcException {
        User user = getUser(userId);
        List<Message> messages = new ArrayList<>(user.getPrivateMessages());

        if (user.getChannel() != null) {
            messages.addAll(user.getChannel().getChannelMessages());
        }

        return messages.stream().filter(m -> m.getSentDate().isAfter(from)).sorted(Comparator.comparing(Message::getSentDate)).collect(Collectors.toList());
    }

}
