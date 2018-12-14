package org.beny.chat.server.servlet;

import com.caucho.hessian.server.HessianServlet;
import org.beny.chat.common.Config;
import org.beny.chat.server.service.ChatServiceServer;

import javax.servlet.annotation.WebServlet;

@WebServlet(Config.URI_HESSIAN)
public class ChatHessianServlet extends HessianServlet implements ChatServiceServer {

    @Override
    public String getProtocolName() {
        return "Hessian";
    }

}
