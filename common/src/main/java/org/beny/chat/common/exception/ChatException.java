package org.beny.chat.common.exception;

public class ChatException extends Exception {

    public enum ChatErrors {
        USER_NOT_LOGGED_IN(101, "User is not logged in"),
        NICKNAME_ALREADY_TAKEN(103, "Nickname already taken"),
        USER_NOT_FOUND(104, "User not found"),
        CHANNEL_NAME_ALREADY_TAKEN(201, "A channel with this name already exists"),
        ALREADY_ON_CHANNEL(202, "Already on that channel"),
        NOT_ON_CHANNEL(203, "Not on channel"),
        CHANNEL_NOT_FOUND(204, "Channel not found"),

        UNRECOGNIZED_COMMAND(300, "Unrecognized command!"),
        WRONG_NAME(301, "Entered name is not valid"),

        INTERNAL_SERVER_EXCEPTION(500, "INTERNAL SERVER EXCEPTION");

        private int code;
        private String message;

        ChatErrors(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    private final ChatErrors error;

    public ChatException(ChatErrors error) {
        super(error.name());
        this.error = error;
    }

    public int getErrorCode() {
        return error.code;
    }

    public String getMessage() {
        return error.message;
    }

    @Override
    public String toString() {
        return error.code + ": " + error.message;
    }

}
