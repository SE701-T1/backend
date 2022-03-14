package com.team701.buddymatcher.socket.socketio.server;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

import java.util.HashMap;
import java.util.Map;

public class ChatEventHandler {

    SocketIOServer server;
    Map<Integer, String> userChats = new HashMap(); //Map of UserId to namespace for chat

    public void setupServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8080);

        server = new SocketIOServer(config);

        server.start();
    }

    //TODO
    public void addNewChat() {

    }

    public void closeServer() {
        server.stop();
    }

}

