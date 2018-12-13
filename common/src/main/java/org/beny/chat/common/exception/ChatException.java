package org.beny.chat.common.exception;

public class ChatException extends Exception {

    public enum ChatErrors {
        NICKNAME_ALREADY_TAKEN,
        CHANNEL_NAME_ALREADY_TAKEN,
        CHANNEL_NOT_FOUND,
        USER_NOT_FOUND,
        ALREADY_ON_CHANNEL,
        NOT_ON_CHANNEL,

        INTERNAL_SERVER_EXCEPTION,
        INTERNAL_SERVER_CRITICAL_EXCEPTION
    }

    private ChatErrors error;

    public ChatException(ChatErrors error) {
        super(error.name());
        this.error = error;
    }

    public void setError(ChatErrors error) {
        this.error = error;
    }

    public ChatErrors getError() {
        return error;
    }
}
