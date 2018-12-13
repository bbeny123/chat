package org.beny.chat.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;

import java.net.URL;
import java.time.LocalDateTime;

public enum ChatClient {
    INSTANCE;

    private Long id;
    private ChatService service;
    private LocalDateTime lastSyncDate;

    ChatClient() {
        try
        {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(Config.URL));
            config.setEnabledForExtensions(true);
            config.setEnabledForExceptions(true);
            config.setConnectionTimeout(Config.TIMEOUT);
            config.setReplyTimeout(Config.TIMEOUT);

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            ClientFactory factory = new ClientFactory(client);
            this.service = (ChatService) factory.newInstance(ChatService.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static Long getId() {
        return INSTANCE.id;
    }

    public static void login(Long id) {
        INSTANCE.id = id;
        syncDate();
    }

    public static ChatService getService() {
        return INSTANCE.service;
    }

    public static void syncDate() {
        INSTANCE.lastSyncDate = LocalDateTime.now();
    }

    public static LocalDateTime getSyncDate() {
        return INSTANCE.lastSyncDate;
    }
}
