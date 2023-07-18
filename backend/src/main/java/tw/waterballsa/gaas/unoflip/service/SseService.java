package tw.waterballsa.gaas.unoflip.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tw.waterballsa.gaas.unoflip.vo.BroadcastEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SseService {
    private final Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter registerSseEmitter(String playerId) {
        SseEmitter emitter = new SseEmitter(60L * 1000L);
        emitter.onCompletion(() -> System.out.printf("[%s] SseEmitter is completed%n", playerId));
        emitter.onTimeout(() -> System.out.printf("[%s] SseEmitter is timed out%n", playerId));
        emitter.onError((ex) -> System.out.printf("[%s] SseEmitter got error:%s%n", playerId, ex.getMessage()));
        emitterMap.put(playerId, emitter);
        System.out.printf("[%s] SseEmitter register success%n", playerId);
        return emitter;
    }

    public void sendMessage(BroadcastEvent broadcastEvent) {
        if (null == broadcastEvent.playerIds() || broadcastEvent.playerIds().isEmpty()) {
            return;
        }
        for (String playerId : broadcastEvent.playerIds()) {
            SseEmitter emitter = emitterMap.get(playerId);
            if (emitter == null) {
                return;
            }
            System.out.printf("[%s] send event to %s, data: %s%n", playerId, playerId, broadcastEvent.eventBody());
            try {
                emitter.send(broadcastEvent.eventBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
