package org.beny.chat.server;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.server.service.ChatServiceImpl;

public class Main {

    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer(Config.PORT);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setVoidMethodEnabled(true);
        phm.addHandler(ChatService.class.getName(), ChatServiceImpl.class);
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        serverConfig.setEnabledForExceptions(true);

        webServer.start();
    }

}
