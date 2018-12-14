package org.beny.chat.server.servlet;

import com.caucho.burlap.server.BurlapServlet;
import org.beny.chat.common.Config;
import org.beny.chat.server.service.ChatServiceServer;

import javax.servlet.annotation.WebServlet;

@WebServlet(Config.URI_BURLAP)
public class ChatBurlapServlet extends BurlapServlet implements ChatServiceServer {

    @Override
    public String getProtocolName() {
        return "Burlap";
    }

}
