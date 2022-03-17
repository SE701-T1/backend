package com.team701.buddymatcher.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.HashMap;
import java.util.UUID;

public class ChatEventHandler {

    private final String USER_ID_KEY = "userId";
    private final String MESSAGE_EVENT_KEY = "message";

    SocketIOServer server;
    HashMap<Long, UUID> userConnections = new HashMap<>(); // User ID -> SocketIO Client ID

    public void setupServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        server = new SocketIOServer(config);

        server.addConnectListener(socketIOClient ->
                userConnections.put(socketIOClient.get(USER_ID_KEY), socketIOClient.getSessionId()));

        server.addDisconnectListener(socketIOClient -> userConnections.remove((Long) socketIOClient.get(USER_ID_KEY)));

        server.addEventListener(MESSAGE_EVENT_KEY, MessageObject.class, (client, data, ackRequest) -> {
            SocketIOClient receiver = server.getClient(userConnections.get(data.receiverId));
            if (receiver != null) receiver.sendEvent(MESSAGE_EVENT_KEY, data);
            putMessageInHistory(data);
        });

        server.start();
    }

    private void putMessageInHistory(MessageObject data) {
        //TODO: Implement
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

