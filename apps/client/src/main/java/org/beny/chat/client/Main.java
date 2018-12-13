package org.beny.chat.client;

import org.beny.chat.client.util.FetchMessages;
import org.beny.chat.client.util.InputUtil;
import org.beny.chat.common.Config;
import org.beny.chat.common.exception.ChatException;

import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static void main(String[] args) throws Exception {
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
