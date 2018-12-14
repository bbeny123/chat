package org.beny.chat.client;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import org.beny.chat.client.util.FetchMessages;
import org.beny.chat.client.util.InputUtil;
import org.beny.chat.common.ChatService;
import org.beny.chat.common.Config;
import org.beny.chat.common.exception.ChatException;

import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static void main(String[] args) throws Exception {

        HessianProxyFactory factory = new HessianProxyFactory();
        ChatService basic = (ChatService) factory.create(ChatService.class, Config.URL_HESSIAN);

        BurlapProxyFactory factoryB = new BurlapProxyFactory();
        ChatService bur = (ChatService) factoryB.create(ChatService.class, Config.URL_BURLAP);

        Long hesId = basic.login("beny");
        basic.createChannel(hesId, "a");
        System.out.println(basic.getChannels(hesId));

        Long burId = bur.login("beny2");
        basic.createChannel(burId, "ab");
        System.out.println(bur.getChannels(burId));

        System.out.println(basic.getChannels(hesId));

        new Timer().schedule(new FetchMessages(), Config.FETCH_MESSAGES_PERIOD_IN_MS, Config.FETCH_MESSAGES_PERIOD_IN_MS);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try{
                InputUtil.checkInput(scanner.nextLine());
            } catch (ChatException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

}
