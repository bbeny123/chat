package org.beny.chat.client;

import org.beny.chat.common.exception.ChatException;

import static org.beny.chat.client.ChatClient.getService;

public class Main {

    public static void main(String[] args) throws Exception {
//        Util.checkInput(new Scanner(System.in).next());

        getService().login("Beny");
//        getService().channelMessage(ChatClient.getId(), "marian");
        try {
            getService().joinChannel(ChatClient.getId(),"marian");
        } catch (ChatException ex) {
            System.out.println(ex);
            Throwable a = ex.getCause();
            System.out.println(ex.getCause());
        } catch (Exception ex) {
            System.out.println(ex);
            Throwable a = ex.getCause();
            System.out.println(ex.getCause());
        }
//
//        channelMessage("xddddddd");
//        System.out.println(getChannels());
//        System.out.println(getChannelUsers());
//        System.out.println(getMessages());
    }

}
