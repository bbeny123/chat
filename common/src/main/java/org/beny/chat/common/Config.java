package org.beny.chat.common;

public interface Config {

    int MAX_GENERATION_ATTEMPTS = 10;
    int MAX_INACTIVE_TIME_IN_SEC = 10;
    int REMOVE_INACTIVE_TASK_PERIOD_IN_MS = 10 * 1000;
    int FETCH_MESSAGES_PERIOD_IN_MS = 300;
    int TIMEOUT = 60 * 1000;
    String URL = "http://127.0.0.1:8080/chat";
    String URI_XMLRPC = "/xmlrpc";
    String URL_XMLRPC = URL + URI_XMLRPC;
    String URI_HESSIAN = "/hessian";
    String URL_HESSIAN = URL + URI_HESSIAN;
    String URI_BURLAP = "/burlap";
    String URL_BURLAP = URL + URI_BURLAP;

}
