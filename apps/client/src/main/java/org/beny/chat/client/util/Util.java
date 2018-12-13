package org.beny.chat.client.util;

public class Util {

    public static void checkInput (String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] inputArray = input.split(" ", 2);
        switch (inputArray[0]) {
            case "/login": {
                if (inputArray.length < 2 || inputArray[1].chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
                    // error
                } else {
                    // login with nick inputArray[1]
                }
                break;
            }
        }
    }

}
