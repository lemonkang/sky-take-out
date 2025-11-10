package org.example.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandle extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MyWebSocketHandle.class);
    // 保存所有连接的 session
  public static   Map<String,WebSocketSession> SESSION_MAP= new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        log.info("query:"+query);
        String key;
        if (query != null && query.contains("=")) {
            try {
                key = query.split("=", 2)[1];
            } catch (Exception e) {
                key = session.getId();
            }
        } else {
            key = session.getId();
        }
        SESSION_MAP.put(key, session);
        System.out.println("✅ WebSocket 连接成功：" + SESSION_MAP);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到消息：" + message.getPayload());
        String query = session.getUri().getQuery();


        // 示例：把客户端发来的消息广播给所有人
//        for (WebSocketSession ws : SESSION_MAP.values()) {
//            ws.sendMessage(new TextMessage("服务器收到：" + message.getPayload()));
//        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SESSION_MAP.values().removeIf(s -> s.getId().equals(session.getId()));
        System.out.println(" WebSocket 连接关闭：" + session.getId());
    }
}
