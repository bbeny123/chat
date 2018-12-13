package org.beny.chat.common;

public interface Config {

    int MAX_GENERATION_ATTEMPTS = 10;
    int MAX_INACTIVE_TIME_IN_SEC = 10;
    int REMOVE_INACTIVE_TASK_PERIOD_IN_MS = 30 * 1000;
    int FETCH_MESSAGES_PERIOD_IN_MS = 500;
    int PORT = 66;
    int TIMEOUT = 60 * 1000;
    String URL = "http://127.0.0.1:" + PORT + "/xmlrpc";

}
