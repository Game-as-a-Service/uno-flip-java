package tw.waterballsa.gaas.unoflip.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.waterballsa.gaas.unoflip.presenter.GameJoinPresenter;
import tw.waterballsa.gaas.unoflip.usecase.GameJoinUseCase;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

@RestController
public class GameController {

    private final GameJoinUseCase gameJoinUseCase;
    private final GameJoinPresenter gameJoinPresenter;

    public GameController(GameJoinUseCase gameJoinUseCase, GameJoinPresenter gameJoinPresenter) {
        this.gameJoinUseCase = gameJoinUseCase;
        this.gameJoinPresenter = gameJoinPresenter;
    }

    @PostMapping("join/{playerId}")
    public Response<JoinResult> join(@PathVariable String playerId, @RequestBody JoinRequest joinRequest) {
        return gameJoinPresenter.present(playerId, gameJoinUseCase.join(playerId, joinRequest.playerName()));
    }
}
