package com.team701.buddymatcher.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.team701.buddymatcher.repositories.communication.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
public class ChatEventHandler {
    private static final Logger log = LoggerFactory.getLogger(ChatEventHandler.class);
    private final SocketIONamespace namespace;

    // User ID -> SocketIO Client ID
    private final HashMap<Long, UUID> userConnections = new HashMap<>();

    // Keys
    private final String USER_ID_KEY = "userId";

    // Events
    private final String MESSAGE_EVENT_KEY = "message";


    @Autowired
    public ChatEventHandler(SocketIOServer server, MessageRepository messageRepository) {
        this.namespace = server.addNamespace("/chat");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener(MESSAGE_EVENT_KEY, MessageObject.class, onMessageReceived());
    }

    private DataListener<MessageObject> onMessageReceived() {
        return (client, data, ackSender) -> {
            log.debug("Client[{}] - Received chat message '{}'", client.getSessionId().toString(), data);

            SocketIOClient receiver = namespace.getClient(userConnections.get(data.receiverId));
            if (receiver != null) receiver.sendEvent(MESSAGE_EVENT_KEY, data);
            putMessageInHistory(data);
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to chat handler through '{}'", client.getSessionId().toString(), handshakeData.getUrl());

            userConnections.put(client.get(USER_ID_KEY), client.getSessionId());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from chat handler.", client.getSessionId().toString());

            userConnections.remove((Long) client.get(USER_ID_KEY));
        };
    }

    private void putMessageInHistory(MessageObject data) {
        //TODO: Implement
    }
}

