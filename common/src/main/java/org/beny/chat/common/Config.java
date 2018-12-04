package org.beny.chat.common;

public interface Config {

    int MAX_GENERATION_ATTEMPTS = 10;
    int PORT = 66;
    int TIMEOUT = 60 * 1000;
    String HANDLER = "chat";
    String URL = "http://127.0.0.1:" + PORT + "/xmlrpc";

}
