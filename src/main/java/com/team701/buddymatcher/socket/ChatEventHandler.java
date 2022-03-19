package com.team701.buddymatcher.socket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.communication.MessageRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
public class ChatEventHandler {
    private static final Logger log = LoggerFactory.getLogger(ChatEventHandler.class);
    private final SocketIOServer namespace;

    // Maintain two hashmap for bidirectional connections <-> userId
    private final HashMap<Long, UUID> userIdConnections = new HashMap<>();
    private final HashMap<UUID, Long> connectionUserIds = new HashMap<>();

    // Keys
    private final String JWT_KEY = "jwt";

    // Events
    private final String MESSAGE_EVENT_KEY = "message";
    private final String ONLINE_EVENT_KEY = "online";
    private final String READ_EVENT_KEY = "read";


    private MessageRepository messageRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public ChatEventHandler(SocketIOServer server, MessageRepository messageRepository) {
        this.namespace = server;
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener(MESSAGE_EVENT_KEY, MessageObject.class, onMessageReceived());
        this.namespace.addEventListener(READ_EVENT_KEY, ReadObject.class, onReadReceived());

        this.messageRepository = messageRepository;

        log.info("SocketIO ChatEventHandler Started");
    }

    /**
     * onMessageReceived listener that sends message to the receiver when it receives a message event
     * @return
     */
    private DataListener<MessageObject> onMessageReceived() {
        return (client, data, ackSender) -> {
            Long userId = connectionUserIds.get(client.getSessionId());
            log.info("Client[{}] User[{}] - Receiver[{}] message '{}'", client.getSessionId().toString(), userId, data.getReceiverId(), data.getMessage());
            data.setSenderId(userId);

            UUID connection = userIdConnections.get(data.getReceiverId());
            if (connection == null) {
                putMessageInHistory(data);
                return;
            }

            SocketIOClient receiver = namespace.getClient(connection);
            if (receiver != null) sendMessage(receiver, data);
            putMessageInHistory(data);
        };
    }

    /**
     * onReadReceived listener that sends read notice to the buddy when it receives a read event
     * @return
     */
    private DataListener<ReadObject> onReadReceived() {
        return (client, data, ackSender) -> {
            Long userId = connectionUserIds.get(client.getSessionId());
            log.info("Client[{}] User[{}] - Read messages", client.getSessionId().toString(), userId);

            messageRepository.updateUnreadMessagesForAUser(userId, data.getBuddyId());

            SocketIOClient receiver = namespace.getClient(userIdConnections.get(data.getBuddyId()));
            if (receiver != null) sendRead(receiver, new ReadObject().setBuddyId(userId));
        };
    }

    /**
     * onConnected listener that stores current user connected and reports online users to all users
     * @return
     */
    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            String jwt = handshakeData.getSingleUrlParam(JWT_KEY);
            UUID clientId = client.getSessionId();

            try {
                JwtTokenUtil util = new JwtTokenUtil();
                String userId = util.getIdFromToken(jwt);

                if (userId == null || userId == "") {
                    log.info("Client[{}] - userId invalid", clientId.toString());
                    client.disconnect();
                    return;
                };

                log.info("Client[{}] - userId[{}]", clientId.toString(), userId);

                userIdConnections.put(Long.parseLong(userId), client.getSessionId());
                connectionUserIds.put(client.getSessionId(), Long.parseLong(userId));

                log.info("Client[{}] User[{}] - Connected to chat handler through '{}'", clientId.toString(), userId, handshakeData.getUrl());

                sendOnline();
            } catch(NumberFormatException nfe) {
                log.info("Client[{}] - userId invalid", clientId.toString());
                client.disconnect();
                return;
            } catch (ExpiredJwtException eje) {
                log.info("Client[{}] - jwt expired", clientId.toString());
                client.disconnect();
                return;
            }
        };
    }

    /**
     * onDisconnected listener removes current user from store and reports online users to all users.
     * @return
     */
    private DisconnectListener onDisconnected() {
        return client -> {
            UUID clientId = client.getSessionId();
            Long userId = connectionUserIds.get(clientId);
            log.info("Client[{}] User[{}] - Disconnected from chat handler.", clientId.toString(), userId);

            userIdConnections.remove(clientId);
            connectionUserIds.remove(userId);

            sendOnline();
        };
    }

    private void sendMessage(SocketIOClient receiver, MessageObject message) {
        receiver.sendEvent(MESSAGE_EVENT_KEY, message);
    }

    private void sendOnline() {
        List<UserObject> users = userIdConnections.keySet().stream()
                .map(user -> new UserObject().setUserId(user))
                .toList();

        namespace.getBroadcastOperations().sendEvent(ONLINE_EVENT_KEY, users);
    }

    private void sendRead(SocketIOClient receiver, ReadObject read) {
        receiver.sendEvent(READ_EVENT_KEY, read);
    }

    private void putMessageInHistory(MessageObject data) {
        messageRepository.createMessage(data.getSenderId(), data.getReceiverId(), data.getMessage());
    }
}

