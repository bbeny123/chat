package org.beny.chat.client.util;

import org.beny.chat.client.ChatClient;
import org.beny.chat.common.exception.ChatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.beny.chat.common.exception.ChatException.ChatErrors.UNRECOGNIZED_COMMAND;
import static org.beny.chat.common.exception.ChatException.ChatErrors.WRONG_NAME;

public class InputUtil {

    private static final Map<String, CommandConsumer<String[]>> commands;

    static {
        commands = new HashMap<>();
        commands.put("/login", InputUtil::login);
        commands.put("/channels", InputUtil::channelList);
        commands.put("/join", InputUtil::joinChannel);
        commands.put("/channel", InputUtil::createChannel);
        commands.put("/users", InputUtil::userList);
        commands.put("/w", InputUtil::whisper);
        commands.put("/p", InputUtil::changeProtocol);
        commands.put("/protocols", InputUtil::protocols);
        commands.put("/help", InputUtil::help);
        commands.put("/logout", InputUtil::logout);
        commands.put("/exit", InputUtil::exit);
    }

    public static void checkInput(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] inputArray = input.split(" ", 2);
        if (!inputArray[0].trim().startsWith("/")) {
            ChatClient.channelMessage(input.trim());
        } else if (commands.containsKey(inputArray[0])) {
            commands.get(inputArray[0]).accept(inputArray);
        } else {
            throw new ChatException(UNRECOGNIZED_COMMAND);
        }
    }

    private static void login(String[] inputArray) throws Exception {
        validNameArgumentCommand(inputArray);

        if (ChatClient.login(inputArray[1].trim()) != null) {
            info("Welcome " + inputArray[1].trim()+ "! Type /help to see command list");
        }
    }

    private static void channelList(String[] inputArray) throws Exception {
        validNoArgumentCommand(inputArray);

        List<String> channels = ChatClient.getChannels();
        info(channels.isEmpty() ? "No available channels" : "Available channels:");
        channels.forEach(System.out::println);
    }

    private static void joinChannel(String[] inputArray) throws Exception {
        validNameArgumentCommand(inputArray);

        if (ChatClient.joinChannel(inputArray[1].trim())) {
            info("Joined channel: " + inputArray[1]);
        }
    }

    private static void createChannel(String[] inputArray) throws Exception {
        validNameArgumentCommand(inputArray);

        if (ChatClient.createChannel(inputArray[1].trim())) {
            info("Created and joined channel: " + inputArray[1]);
        }
    }

    private static void userList(String[] inputArray) throws Exception {
        validNoArgumentCommand(inputArray);

        List<String> users = ChatClient.getChannelUsers();
        info("Users on the channel:");
        users.forEach(System.out::println);
    }

    private static void whisper(String[] inputArray) throws Exception {
        if (inputArray.length < 2) {
            throw new ChatException(UNRECOGNIZED_COMMAND);
        }
        String[] whisperArray = inputArray[1].split(" ", 2);
        if (whisperArray.length < 2 || whisperArray[1].trim().length() <= 0) {
            throw new ChatException(UNRECOGNIZED_COMMAND);
        }
        if (whisperArray[0].trim().chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            throw new ChatException(WRONG_NAME);
        }
        ChatClient.privateMessage(whisperArray[0], whisperArray[1]);
    }

    private static void changeProtocol(String[] inputArray) throws Exception {
        if (inputArray.length < 2 || inputArray[1].trim().length() == 0) {
            info("Active protocol: " + ChatClient.getProtocol());
            return;
        } else if (Stream.of(Protocols.values()).noneMatch(p -> p.name().equals(inputArray[1].trim().toUpperCase()))) {
            throw new ChatException(WRONG_NAME);
        }

        ChatClient.setService(Protocols.valueOf(inputArray[1].trim().toUpperCase()));
        info("Active protocol changed to: " + inputArray[1].trim().toUpperCase());
    }

    private static void protocols(String[] inputArray) throws Exception {
        validNoArgumentCommand(inputArray);

        info("Available protocols:");
        Stream.of(Protocols.values()).forEach(System.out::println);
    }

    private static void help(String[] inputArray) throws Exception {
        validNoArgumentCommand(inputArray);

        info("Available commands:");
        commands.keySet().forEach(System.out::println);
    }

    private static void logout(String[] inputArray) throws Exception {
        validNameArgumentCommand(inputArray);

        if (ChatClient.logout()) {
            info("You have been logged out!");
        }
    }

    private static void exit(String[] inputArray) throws Exception {
        validNoArgumentCommand(inputArray);

        if (ChatClient.getId() != null) {
            ChatClient.logout();
        }
        System.exit(0);
    }

    private static void info(String string) {
        System.out.println(String.format("[%s] [INFO] " + string, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }


    private static void validNameArgumentCommand(String[] inputArray) throws ChatException {
        if (inputArray.length < 2 || inputArray[1].trim().chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            throw new ChatException(WRONG_NAME);
        }
    }

    private static void validNoArgumentCommand(String[] inputArray) throws ChatException {
        if (inputArray.length > 1 && inputArray[1].trim().length() > 0) {
            throw new ChatException(UNRECOGNIZED_COMMAND);
        }
    }

}
