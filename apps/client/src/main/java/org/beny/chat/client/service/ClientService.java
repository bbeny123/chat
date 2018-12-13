package org.beny.chat.client.service;

public class ClientService {

//    private static XmlRpcClient client = getClient();
//
//    private static Object serverCall(Methods method, Object... objects) throws Exception {
//        return client.execute(methodHandler(method), objects);
//    }
//
//    public static void login(String nick) throws Exception {
//        ChatClient.login((Long) serverCall(LOGIN, nick));
//        System.out.println(getId());
//    }
//
//    public static boolean logout() throws Exception {
//        return (boolean) serverCall(LOGOUT, getId());
//    }
//
//    public static boolean createChannel(String name) throws Exception {
//        return (boolean) serverCall(CHANNEL_CREATE, getId(), name);
//    }
//
//    public static boolean joinChannel(String name) throws Exception {
//        return (boolean) serverCall(CHANNEL_JOIN, getId(), name);
//    }
//
//    public static boolean channelMessage(String message) throws Exception {
//        return (boolean) serverCall(MESSAGE_CHANNEL, getId(), message);
//    }
//
//    public static boolean privateMessage(String target, String message) throws Exception {
//        return (boolean) serverCall(MESSAGE_CHANNEL, getId(), target, message);
//    }
//
//    public static List<String> getChannels() throws Exception {
//        return Stream.of(((Object[]) serverCall(CHANNELS, getId()))).map(c -> (String) c).collect(Collectors.toList());
//    }
//
//    public static List<String> getChannelUsers() throws Exception {
//        return Stream.of(((Object[]) serverCall(USERS, getId()))).map(u -> (String) u).collect(Collectors.toList());
//    }
//
//    public static List<Message> getMessages() throws Exception {
//        List<Message> messages = Stream.of(((Object[]) serverCall(MESSAGES, getId(), getSyncDate().minusHours(1)))).map(u -> (Message) u).collect(Collectors.toList());
//        syncDate();
//        return messages;
//    }

}
