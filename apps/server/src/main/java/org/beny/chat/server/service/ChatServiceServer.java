package org.beny.chat.server.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.common.domain.Channel;
import org.beny.chat.common.domain.Message;
import org.beny.chat.common.domain.User;
import org.beny.chat.common.exception.ChatException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.beny.chat.common.exception.ChatException.ChatErrors.*;
import static org.beny.chat.server.ChatServer.INSTANCE;

public interface ChatServiceServer extends ChatService {

    String getProtocolName();

    default Logger getLogger() {
        return LogManager.getLogger(getProtocolName());
    }

    default User getUser(Long userId) throws ChatException {
        User user = INSTANCE.getUsers().entrySet().stream().filter(e -> e.getKey().equals(userId)).findAny().orElseThrow(() -> new ChatException(USER_NOT_LOGGED_IN)).getValue();
        user.setLastActivity(LocalDateTime.now());
        return user;
    }

    default Long login(String nickname) throws ChatException {
        if (INSTANCE.getUsers().entrySet().stream().anyMatch(e -> e.getValue().getNickname().equals(nickname))) {
            throw new ChatException(NICKNAME_ALREADY_TAKEN);
        }

        for (int i = 0; i <= Config.MAX_GENERATION_ATTEMPTS; i++) {
            Long id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
            if (!INSTANCE.getUsers().containsKey(id)) {
                INSTANCE.getUsers().put(id, new User(id, nickname));

                getLogger().info(String.format("User logged - nick: %s (%d)", nickname, id));

                return id;
            }
        }

        throw new ChatException(INTERNAL_SERVER_EXCEPTION);
    }

    default boolean logout(Long userId) throws ChatException {
        User user = getUser(userId);

        if (user.getChannel() != null) {
            user.getChannel().getUsers().remove(user);
        }
        INSTANCE.getUsers().remove(userId);

        getLogger().info(String.format("User logged out - nick: %d", userId));

        return true;
    }

    default boolean createChannel(Long userId, String channelName) throws ChatException {
        if (INSTANCE.getChannels().containsKey(channelName)) {
            throw new ChatException(CHANNEL_NAME_ALREADY_TAKEN);
        }

        User user = getUser(userId);

        Channel channel = new Channel(channelName, user);
        INSTANCE.getChannels().put(channelName, channel);
        user.setChannel(channel);

        getLogger().info(String.format("Channel created - user: %s (%d); channel: %s", user.getNickname(), userId, channelName));

        return true;
    }

    default boolean joinChannel(Long userId, String channelName) throws ChatException {
        if (!INSTANCE.getChannels().containsKey(channelName)) {
            throw new ChatException(CHANNEL_NOT_FOUND);
        }

        User user = getUser(userId);

        if (user.getChannel() != null && user.getChannel().getName().equals(channelName)) {
            throw new ChatException(ALREADY_ON_CHANNEL);
        }

        if (user.getChannel() != null) {
            INSTANCE.getChannels().get(user.getChannel().getName()).getUsers().removeIf(u -> u.getId().equals(userId));
        }

        Channel channel = INSTANCE.getChannels().get(channelName);
        user.setChannel(channel);
        channel.getUsers().add(user);

        channel.getChannelMessages().add(new Message(user.getNickname() + " entered channel!", null, Message.MessageTypes.ADMIN));

        getLogger().info(String.format("User joined channel - user: %s (%d); channel: %s", user.getNickname(), userId, channelName));

        return true;
    }

    default boolean channelMessage(Long userId, String message) throws ChatException {
        User user = getUser(userId);
        if (user.getChannel() == null) {
            throw new ChatException(NOT_ON_CHANNEL);
        }

        user.getChannel().getChannelMessages().add(new Message(message, user.getNickname(), Message.MessageTypes.CHANNEL));

        getLogger().info(String.format("Channel message - user: %s (%d); message: %s", user.getNickname(), userId, message));

        return true;
    }

    default boolean privateMessage(Long userId, String targetNickname, String message) throws ChatException {
        User user = getUser(userId);
        User target = INSTANCE.getUsers().entrySet().stream().filter(e -> e.getValue().getNickname().equals(targetNickname)).findAny().orElseThrow(() -> new ChatException(USER_NOT_FOUND)).getValue();

        user.getPrivateMessages().add(new Message(message, target.getNickname(), Message.MessageTypes.WHISPER_TO));
        target.getPrivateMessages().add(new Message(message, user.getNickname(), Message.MessageTypes.WHISPER_FROM));

        getLogger().info(String.format("Private message - user: %s (%d); target: %s (%d); message: %s", user.getNickname(), userId, targetNickname, target.getId(), message));

        return true;
    }

    default List<String> getChannels(Long userId) throws ChatException {
        getUser(userId);

        getLogger().info(String.format("Channels requested - user: %d", userId));

        return INSTANCE.getChannels().entrySet().stream().map(e -> e.getValue().getName()).collect(Collectors.toList());
    }

    default List<String> getChannelUsers(Long userId) throws ChatException {
        getLogger().info(String.format("Channel users requested - user: %d", userId));

        return getUser(userId).getChannel().getUsers().stream().map(User::getNickname).collect(Collectors.toList());
    }

    default List<Message> getMessages(Long userId, LocalDateTime from) throws ChatException {
        User user = getUser(userId);
        List<Message> messages = new ArrayList<>(user.getPrivateMessages());

        if (user.getChannel() != null) {
            messages.addAll(user.getChannel().getChannelMessages());
        }

        getLogger().info(String.format("Messages requested - user: %s (%d)", user.getNickname(), userId));

        return messages.stream().filter(m -> m.getSentDate().isAfter(from)).sorted(Comparator.comparing(Message::getSentDate)).collect(Collectors.toList());
    }

}
