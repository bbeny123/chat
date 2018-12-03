package org.beny.chat.common;

import static org.beny.chat.common.Config.HANDLER;

public class ChatUtil {

    public static String methodHandler(String methodName) {
        return HANDLER + "." + methodName;
    }

}
