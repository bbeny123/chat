package org.beny.chat.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.beny.chat.common.ChatUtil;
import org.beny.chat.common.Config;

import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(Config.URL));
        config.setEnabledForExtensions(true);
        config.setContentLengthOptional(false);
        config.setConnectionTimeout(Config.TIMEOUT);
        config.setReplyTimeout(Config.TIMEOUT);

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        client.execute(ChatUtil.methodHandler("demo"), new Object[0]);
    }

}
