package tw.waterballsa.gaas.unoflip.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.waterballsa.gaas.unoflip.enums.StatusCode;
import tw.waterballsa.gaas.unoflip.service.GameService;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("join/{playerId}")
    public Response<JoinResult> join(@PathVariable String playerId, @RequestBody JoinRequest joinRequest) {
        return new Response<>(StatusCode.OK.getCode(), "join successfully", gameService.join(playerId, joinRequest.playerName()));
    }
}
