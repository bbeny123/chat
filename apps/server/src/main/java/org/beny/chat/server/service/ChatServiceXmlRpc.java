package org.beny.chat.server.service;

public class ChatServiceXmlRpc implements ChatServiceServer {

    @Override
    public String getProtocolName() {
        return "XmlRpc";
    }

}
