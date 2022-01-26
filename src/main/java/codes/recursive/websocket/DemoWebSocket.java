package codes.recursive.websocket;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;

import java.util.function.Predicate;

@ServerWebSocket("/ws/{topic}")
public class DemoWebSocket {
    private final WebSocketBroadcaster broadcaster;

    public DemoWebSocket(
            WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(
            String topic,
            WebSocketSession session) {
        broadcaster.broadcastSync(CollectionUtils.mapOf("joined", true), isValid(topic));
    }

    @OnMessage
    public void onMessage(
            String topic,
            String message,
            WebSocketSession session) {
        broadcaster.broadcastSync(message, isValid(topic));
    }

    @OnClose
    public void onClose(
            String topic,
            WebSocketSession session) {
        broadcaster.broadcastSync(CollectionUtils.mapOf("closed", true), isValid(topic));
    }

    private Predicate<WebSocketSession> isValid(
            String topic) {
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
