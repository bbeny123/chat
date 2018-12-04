package org.beny.chat.common;

public enum Methods {

    LOGIN("login"),
    LOGOUT("logout"),
    CHANNEL_CREATE("createChannel"),
    CHANNEL_JOIN("joinChannel"),
    MESSAGE_CHANNEL("channelMessage"),
    MESSAGE_PRIVATE("privateMessage"),
    CHANNELS("getChannels"),
    USERS("getChannelUsers"),
    MESSAGES("getMessages");

    private final String methodName;

    Methods(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
