package org.beny.chat.server.servlet;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.server.service.ChatServiceXmlRpc;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet(Config.URI_XMLRPC)
public class ChatXmlRpcServlet extends XmlRpcServlet {

    @Override
    public XmlRpcServletServer newXmlRpcServer(ServletConfig pConfig) {
        XmlRpcServletServer server = new XmlRpcServletServer();

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) server.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setEnabledForExceptions(true);
        serverConfig.setContentLengthOptional(false);

        return server;
    }

    @Override
    protected XmlRpcHandlerMapping newXmlRpcHandlerMapping() throws XmlRpcException {
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setVoidMethodEnabled(true);

        phm.addHandler(ChatService.class.getName(), ChatServiceXmlRpc.class);
        return phm;
    }
}
