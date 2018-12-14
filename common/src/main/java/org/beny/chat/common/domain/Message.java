package org.beny.chat.common.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    private Date sentDate;
    private int messageType;

    public Message(String message, String source, int messageType) {
        this.message = message;
        this.source = source;
        this.sentDate = new Date();
        this.messageType = messageType;
    }

    public Date getSentDate() {
        return sentDate;
    }

    @Override
    public String toString() {
        return "[" + new Timestamp(sentDate.getTime()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] "
                + (messageType == ADMIN ? "[INFO] " : messageType == WHISPER_FROM ? "[W] ": messageType == WHISPER_TO ? "[W TO] " : "")
                + (source != null ? source + ": " : "")
                + message;
    }
}
