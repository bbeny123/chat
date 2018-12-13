package org.beny.chat.client;

import static org.beny.chat.client.service.ClientService.*;

public class Main {

    public static void main(String[] args) throws Exception {
//        Util.checkInput(new Scanner(System.in).next());

        login("Beny2");
        createChannel("mariusz2");
        joinChannel("mariusz");
        channelMessage("asd");
        channelMessage("bsd");
        System.out.println(getMessages());
        System.out.println(getChannels());
        System.out.println(getChannelUsers());
//        getService().channelMessage(ChatClient.getId(), "marian");
//
//        channelMessage("xddddddd");
//        System.out.println(getChannels());
//        System.out.println(getChannelUsers());
//        System.out.println(getMessages());
    }

}
