package tw.waterballsa.gaas.unoflip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.waterballsa.gaas.unoflip.presenter.GameJoinPresenter;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.usecase.GameJoinUseCase;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameJoinUseCase gameJoinUseCase;
    private final GameJoinPresenter gameJoinPresenter;
    private final SseService sseService;

    @PostMapping("join/{playerId}")
    public Response<JoinResult> join(@PathVariable String playerId, @RequestBody JoinRequest joinRequest) {
        GameJoinResult gameJoinResult = gameJoinUseCase.join(playerId, joinRequest.playerName());
        sseService.sendMessage(gameJoinPresenter.broadcastEvent(playerId, gameJoinResult));
        return gameJoinPresenter.response(playerId, gameJoinResult);
    }
}
