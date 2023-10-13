package tw.waterballsa.gaas.unoflip.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tw.waterballsa.gaas.unoflip.service.SseService;

@RestController
@RequestMapping("/sse")
public class SSEController {
    private final SseService sseService;

    public SSEController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "{playerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<SseEmitter> broadcast(@PathVariable String playerId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Accel-Buffering", "no");
        httpHeaders.setCacheControl(CacheControl.noCache());

        SseEmitter emitter = sseService.registerSseEmitter(playerId);

        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM).headers(httpHeaders).body(emitter);
    }
}
