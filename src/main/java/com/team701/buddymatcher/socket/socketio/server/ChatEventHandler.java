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
        config.setPort(9092);

        server = new SocketIOServer(config);

        //For testing
        /*
        final SocketIONamespace chat1namespace = server.addNamespace("/chat1");
        chat1namespace.addEventListener("message", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                // broadcast messages to all clients
                chat1namespace.getBroadcastOperations().sendEvent("message", data);
            }
        });

         */

        server.start();
    }

    //TODO
    public void addNewChat() {

    }

    public void closeServer() {
        server.stop();
    }


    /**
     * For testing
     */
    public static void main(String[] args) throws InterruptedException {

        ChatEventHandler handler = new ChatEventHandler();
        handler.setupServer();

        Thread.sleep(Integer.MAX_VALUE);
        handler.closeServer();

    }

}

