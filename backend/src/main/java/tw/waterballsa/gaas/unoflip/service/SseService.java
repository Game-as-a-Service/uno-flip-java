package tw.waterballsa.gaas.unoflip.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.PersonalEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            Optional.ofNullable(emitterMap.get(playerId)).ifPresent(emitter -> {
                System.out.printf("[broadcast] send eventBody to %s, data: %s%n", playerId, broadcastEvent.eventBody());
                try {
                    emitter.send(broadcastEvent.eventBody());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void sendMessage(PersonalEvent personalEvent) {
        Optional.ofNullable(emitterMap.get(personalEvent.playerId())).ifPresent(emitter -> {
            System.out.printf("[personal] send eventBody to %s, data: %s%n", personalEvent.playerId(), personalEvent.eventBody());
            try {
                emitter.send(personalEvent.eventBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
