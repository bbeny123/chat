package org.beny.chat.client.util;

import org.beny.chat.client.ChatClient;
import org.beny.chat.common.exception.ChatException;

import java.util.List;

import static org.beny.chat.client.service.ClientService.*;
import static org.beny.chat.common.exception.ChatException.ChatErrors.UNRECOGNIZED_COMMAND;
import static org.beny.chat.common.exception.ChatException.ChatErrors.WRONG_NAME;

public class InputUtil {

    public static void checkInput (String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] inputArray = input.split(" ", 2);
        if (!inputArray[0].trim().startsWith("/")) {
            channelMessage(input.trim());
            return;
        }
        switch (inputArray[0]) {
            case "/quit":
            case "/exit": {
                validNoArgumentCommand(inputArray);
                if (ChatClient.getId() != null) {
                    logout();
                }
                System.exit(0);
                break;
            }
            case "/login": {
                validNameArgumentCommand(inputArray);
                if (login(inputArray[1].trim()) != null) {
                    System.out.println("WELCOME!");
                }
                break;
            }
            case "/logout": {
                validNoArgumentCommand(inputArray);
                if (logout()) {
                    System.out.println("YOU HAVE BEEN LOGGED OUT!");
                }
                break;
            }
            case "/create": {
                validNameArgumentCommand(inputArray);
                if (createChannel(inputArray[1].trim())) {
                    System.out.println("CREATED AND ENTERED CHANNEL: " + inputArray[1]);
                }
                break;
            }
            case "/channel": {
                validNameArgumentCommand(inputArray);
                if (joinChannel(inputArray[1].trim())) {
                    System.out.println("ENTERED CHANNEL: " + inputArray[1]);
                }
                break;
            }
            case "/w":
            case "/whisper": {
                String[] whisperArray = input.split(" ", 3);
                if (whisperArray.length < 3 || whisperArray[2].trim().length() <= 0) {
                    throw new ChatException(UNRECOGNIZED_COMMAND);
                }
                if (whisperArray[1].trim().chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
                    throw new ChatException(WRONG_NAME);
                }
                privateMessage(whisperArray[1], whisperArray[2]);
                break;
            }
            case "/channels": {
                validNoArgumentCommand(inputArray);

                List<String> channels = getChannels();
                System.out.println(channels.isEmpty() ? "NO AVAILABLE CHANNELS" : "AVAILABLE CHANNELS:");
                channels.forEach(System.out::println);
                break;
            }
            case "/users": {
                validNoArgumentCommand(inputArray);

                List<String> users = getChannelUsers();
                System.out.println("USER ON THE CHANNEL:");
                users.forEach(System.out::println);
                break;
            }
            case "/help":
            case "/commands": {
                System.out.println("COMMANDS:");
                System.out.println("/login <nickname>");
                System.out.println("/channels");
                System.out.println("/create <channel name>");
                System.out.println("/channel <channel name>");
                System.out.println("/users");
                System.out.println("<message>");
                System.out.println("/whisper <target name> <message>");
                System.out.println("/w <target name> <message>");
                System.out.println("/logout");
                System.out.println("/exit");
                System.out.println("/quit");
                break;
            }
            default: {
                throw new ChatException(UNRECOGNIZED_COMMAND);
            }
        }
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
