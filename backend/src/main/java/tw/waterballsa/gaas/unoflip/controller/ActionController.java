package tw.waterballsa.gaas.unoflip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;
import tw.waterballsa.gaas.unoflip.usecase.DrawUseCase;

@RestController
@RequestMapping("action")
@RequiredArgsConstructor
public class ActionController {
    private final DrawUseCase drawUseCase;

    @PostMapping("draw/{tableId}/{actionPlayerId}")
    public Response<DrawResult> draw(@PathVariable int tableId, @PathVariable String actionPlayerId) {
        return drawUseCase.draw(tableId, actionPlayerId);
    }
}
