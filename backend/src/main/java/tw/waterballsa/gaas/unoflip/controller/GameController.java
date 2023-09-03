package tw.waterballsa.gaas.unoflip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.waterballsa.gaas.unoflip.usecase.GameJoinUseCase;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.response.JoinResult;
import tw.waterballsa.gaas.unoflip.response.Response;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameJoinUseCase gameJoinUseCase;

    @PostMapping("join/{playerId}")
    public Response<JoinResult> join(@PathVariable String playerId, @RequestBody JoinRequest joinRequest) {
        return gameJoinUseCase.join(playerId, joinRequest.playerName());
    }
}
