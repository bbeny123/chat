package org.beny.chat.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.beny.chat.common.domain.Message.MessageTypes.*;

public class Message implements Serializable {

    public interface MessageTypes {
        int ADMIN = 0;
        int CHANNEL = 1;
        int WHISPER_TO = 2;
        int WHISPER_FROM = 3;
    }

    private String message;
    private String source;
    private LocalDateTime sentDate;
    private int messageType;

    public Message(String message, String source, int messageType) {
        this.message = message;
        this.source = source;
        this.sentDate = LocalDateTime.now();
        this.messageType = messageType;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    @Override
    public String toString() {
        return "[" + sentDate.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] "
                + (messageType == ADMIN ? "[INFO] " : messageType == WHISPER_FROM ? "[W] ": messageType == WHISPER_TO ? "[W TO] " : "")
                + (source != null ? source + ": " : "")
                + message;
    }
}
