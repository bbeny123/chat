package org.beny.chat.common.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private String message;
    private String source;
    private LocalDateTime sentDate;
    private boolean privateMessage;

    public Message(String message, String source, boolean privateMessage) {
        this.message = message;
        this.source = source;
        this.sentDate = LocalDateTime.now();
        this.privateMessage = privateMessage;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    @Override
    public String toString() {
        return "[" + sentDate.format(DateTimeFormatter.ISO_LOCAL_TIME) + "] "
                + (privateMessage ? "[W] " : "")
                + source + ": "
                + message;
    }
}
