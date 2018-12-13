package org.beny.chat.server.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.common.domain.Channel;
import org.beny.chat.common.domain.Message;
import org.beny.chat.common.domain.User;
import org.beny.chat.common.exception.ChatException;
import org.beny.chat.server.ChatServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.beny.chat.common.exception.ChatException.ChatErrors.*;

public class ChatServiceImpl implements ChatService {

    private final Logger logger = LogManager.getLogger(getClass());
    private final Map<String, Channel> channels = ChatServer.INSTANCE.getChannels();
    private final Map<Long, User> users = ChatServer.INSTANCE.getUsers();

    private User getUser(Long userId) throws ChatException {
        User user = users.entrySet().stream().filter(e -> e.getKey().equals(userId)).findAny().orElseThrow(() -> new ChatException(USER_NOT_LOGGED_IN)).getValue();
        user.setLastActivity(LocalDateTime.now());
        return user;
    }

    public Long login(String nickname) throws ChatException {
        if (users.entrySet().stream().anyMatch(e -> e.getValue().getNickname().equals(nickname))) {
            throw new ChatException(NICKNAME_ALREADY_TAKEN);
        }

        for (int i = 0; i <= Config.MAX_GENERATION_ATTEMPTS; i++) {
            Long id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
            if (!users.containsKey(id)) {
                users.put(id, new User(id, nickname));

                logger.info(String.format("User logged - nick: %s (%d)", nickname, id));

                return id;
            }
        }

        throw new ChatException(INTERNAL_SERVER_EXCEPTION);
    }

    public boolean logout(Long userId) throws ChatException {
        User user = getUser(userId);

        if (user.getChannel() != null) {
            user.getChannel().getUsers().remove(user);
        }
        users.remove(userId);

        logger.info(String.format("User logged out - nick: %d", userId));

        return true;
    }

    public boolean createChannel(Long userId, String channelName) throws ChatException {
        if (channels.containsKey(channelName)) {
            throw new ChatException(CHANNEL_NAME_ALREADY_TAKEN);
        }

        User user = getUser(userId);

        Channel channel = new Channel(channelName, user);
        channels.put(channelName, channel);
        user.setChannel(channel);

        logger.info(String.format("Channel created - user: %s (%d); channel: %s", user.getNickname(), userId, channelName));

        return true;
    }

    public boolean joinChannel(Long userId, String channelName) throws ChatException {
        if (!channels.containsKey(channelName)) {
            throw new ChatException(CHANNEL_NOT_FOUND);
        }

        User user = getUser(userId);

        if (user.getChannel() != null && user.getChannel().getName().equals(channelName)) {
            throw new ChatException(ALREADY_ON_CHANNEL);
        }

        if (user.getChannel() != null) {
            channels.get(user.getChannel().getName()).getUsers().removeIf(u -> u.getId().equals(userId));
        }

        Channel channel = channels.get(channelName);
        user.setChannel(channel);
        channel.getUsers().add(user);

        channel.getChannelMessages().add(new Message(user.getNickname() + " entered channel!", null, Message.MessageTypes.ADMIN));

        logger.info(String.format("User joined channel - user: %s (%d); channel: %s", user.getNickname(), userId, channelName));

        return true;
    }

    public boolean channelMessage(Long userId, String message) throws ChatException {
        User user = getUser(userId);
        if (user.getChannel() == null) {
            throw new ChatException(NOT_ON_CHANNEL);
        }

        user.getChannel().getChannelMessages().add(new Message(message, user.getNickname(), Message.MessageTypes.CHANNEL));

        logger.info(String.format("Channel message - user: %s (%d); message: %s", user.getNickname(), userId, message));

        return true;
    }

    public boolean privateMessage(Long userId, String targetNickname, String message) throws ChatException {
        User user = getUser(userId);
        User target = users.entrySet().stream().filter(e -> e.getValue().getNickname().equals(targetNickname)).findAny().orElseThrow(() -> new ChatException(USER_NOT_FOUND)).getValue();

        user.getPrivateMessages().add(new Message(message, target.getNickname(), Message.MessageTypes.WHISPER_TO));
        target.getPrivateMessages().add(new Message(message, user.getNickname(), Message.MessageTypes.WHISPER_FROM));

        logger.info(String.format("Private message - user: %s (%d); target: %s (%d); message: %s", user.getNickname(), userId, targetNickname, target.getId(), message));

        return true;
    }

    public List<String> getChannels(Long userId) throws ChatException {
        getUser(userId);

        logger.info(String.format("Channels requested - user: %d", userId));

        return channels.entrySet().stream().map(e -> e.getValue().getName()).collect(Collectors.toList());
    }

    public List<String> getChannelUsers(Long userId) throws ChatException {
        logger.info(String.format("Channel users requested - user: %d", userId));

        return getUser(userId).getChannel().getUsers().stream().map(User::getNickname).collect(Collectors.toList());
    }

    public List<Message> getMessages(Long userId, LocalDateTime from) throws ChatException {
        User user = getUser(userId);
        List<Message> messages = new ArrayList<>(user.getPrivateMessages());

        if (user.getChannel() != null) {
            messages.addAll(user.getChannel().getChannelMessages());
        }

        logger.info(String.format("Messages requested - user: %s (%d)", user.getNickname(), userId));

        return messages.stream().filter(m -> m.getSentDate().isAfter(from)).sorted(Comparator.comparing(Message::getSentDate)).collect(Collectors.toList());
    }

}
