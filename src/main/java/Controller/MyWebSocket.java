package Controller;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ServerEndpoint("/my-websocket")
public class MyWebSocket {
    private static Map<String, Set<Session>> channels = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnClose
    public void onClose(Session session) {
        removeSessionFromChannels(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        removeSessionFromChannels(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        processMessage(message, session);
    }

    public void processMessage(String message, Session session) {
        // Assuming message format: "subscribe:channel1"
        if (message.startsWith("subscribe:")) {
            String channel = message.substring(10);
            addSessionToChannel(session, channel);
        } else if (message.startsWith("chat:")) {
            String[] parts = message.split(":", 3);
            if (parts.length == 3) {
                String channel = parts[1];
                String chatMessage = parts[2];
                broadcastToChannel(channel, chatMessage);
            }
        } else {
            // Handle other types of messages
        }
    }

    public void addSessionToChannel(Session session, String channel) {
        channels.computeIfAbsent(channel, k -> new HashSet<>()).add(session);
    }

    public void removeSessionFromChannels(Session session) {
        channels.values().forEach(sessions -> sessions.remove(session));
    }

    public static void broadcastToChannel(String channel, String message) {
        Set<Session> channelSessions = channels.get(channel);
        if (channelSessions != null) {
            channelSessions.forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);//gửi message tới channel
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
