package org.beny.chat.client;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.beny.chat.client.util.Protocols;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.common.domain.Message;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

public enum ChatClient {
    INSTANCE;

    private Long id;
    private Calendar lastSyncDate;
    private ChatService xmlRpcClient;
    private ChatService hessianClient;
    private ChatService burlapClient;
    private Protocols protocol = Protocols.XMLRPC;

    ChatClient() {
        try {
            xmlRpcClient = xmlRpcClient();
            hessianClient = hessianClient();
            burlapClient = burlapClient();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Long login(String nick) throws Exception {
        if (getId() != null) {
            logout();
        }
        INSTANCE.id = getService().login(nick);
        INSTANCE.lastSyncDate = Calendar.getInstance();
        return getId();
    }

    public static boolean logout() throws Exception {
        Long id = INSTANCE.id;
        INSTANCE.id = null;
        getService().logout(id);
        return true;
    }

    public static boolean createChannel(String name) throws Exception {
        return getService().createChannel(getId(), name);
    }

    public static boolean joinChannel(String name) throws Exception {
        return getService().joinChannel(getId(), name);
    }

    public static boolean channelMessage(String message) throws Exception {
        return getService().channelMessage(getId(), message);
    }

    public static boolean privateMessage(String target, String message) throws Exception {
        return getService().privateMessage(getId(), target, message);
    }

    public static List<String> getChannels() throws Exception {
        return getService().getChannels(getId());
    }

    public static List<String> getChannelUsers() throws Exception {
        return getService().getChannelUsers(getId());
    }

    public static List<Message> getMessages() throws Exception {
        List<Message> messages = getService().getMessages(getId(), INSTANCE.lastSyncDate);
        INSTANCE.lastSyncDate = Calendar.getInstance();
        return messages;
    }

    private static ChatService getService() {
        return INSTANCE.protocol == Protocols.XMLRPC ? INSTANCE.xmlRpcClient :
                INSTANCE.protocol == Protocols.HESSIAN ? INSTANCE.hessianClient :
                        INSTANCE.burlapClient;
    }

    public static void setService(Protocols protocol) {
        INSTANCE.protocol = protocol;
    }

    public static Protocols getProtocol() {
        return INSTANCE.protocol;
    }

    public static Long getId() {
        return INSTANCE.id;
    }

    private ChatService xmlRpcClient() throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(Config.URL_XMLRPC));
        config.setEnabledForExtensions(true);
        config.setEnabledForExceptions(true);
        config.setConnectionTimeout(Config.TIMEOUT);
        config.setReplyTimeout(Config.TIMEOUT);

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        return (ChatService) new ClientFactory(client).newInstance(ChatService.class);
    }

    private ChatService hessianClient() throws Exception {
        return (ChatService) new HessianProxyFactory().create(ChatService.class, Config.URL_HESSIAN);
    }

    private ChatService burlapClient() throws Exception {
        return (ChatService) new BurlapProxyFactory().create(ChatService.class, Config.URL_BURLAP);
    }
}
