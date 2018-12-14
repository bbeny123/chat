package org.beny.chat.server;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.beny.chat.common.ChatService;
import org.beny.chat.server.service.ChatServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/xmlrpc")
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
        phm.addHandler(ChatService.class.getName(), ChatServiceImpl.class);
        return phm;
    }
}
