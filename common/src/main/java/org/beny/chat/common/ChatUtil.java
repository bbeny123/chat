package org.beny.chat.common;

import static org.beny.chat.common.Config.HANDLER;

public class ChatUtil {

    public static String methodHandler(Methods method) {
        return HANDLER + "." + method.getMethodName();
    }

}
