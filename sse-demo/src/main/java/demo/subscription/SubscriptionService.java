package demo.subscription;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscriptionService {

    private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        return emitter;
    }

    public void notify(Notification notification) {
        emitters.forEach(emitter -> {
            sendNotification(emitter, notification);
        });
    }

    private void sendNotification(SseEmitter emitter, Notification notification) {
        try {
            emitter.send(SseEmitter.event().id(UUID.randomUUID().toString()).data(notification));
        } catch (IOException ex) {
            emitters.remove(emitter);
        }
    }

    public static class Notification {

        private String message;

        public Notification() {
        }

        public Notification(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
