package org.beny.chat.common;

import org.beny.chat.common.domain.Message;
import org.beny.chat.common.exception.ChatException;

import java.util.Date;
import java.util.List;

public interface ChatService {

    Long login(String nickname) throws ChatException;
    boolean logout(Long userId) throws ChatException;
    boolean createChannel(Long userId, String channelName) throws ChatException;
    boolean joinChannel(Long userId, String channelName) throws ChatException;
    boolean channelMessage(Long userId, String message) throws ChatException;
    boolean privateMessage(Long userId, String targetNickname, String message) throws ChatException;
    List<String> getChannels(Long userId) throws ChatException;
    List<String> getChannelUsers(Long userId) throws ChatException;
    List<Message> getMessages(Long userId, Date from) throws ChatException;

}
