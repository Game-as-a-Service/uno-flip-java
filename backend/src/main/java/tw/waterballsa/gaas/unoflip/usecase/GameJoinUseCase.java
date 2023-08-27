package tw.waterballsa.gaas.unoflip.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.presenter.GameJoinPresenter;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

@Service
@RequiredArgsConstructor
public class GameJoinUseCase {
    private final GameRepo gameRepo;
    private final GameJoinPresenter gameJoinPresenter;
    private final SseService sseService;

    public Response<JoinResult> join(String playerId, String playerName) {
        UnoFlipGame availableGame = gameRepo.getAvailable().orElseGet(() -> new UnoFlipGame(gameRepo.generateTableId()));
        availableGame.join(playerId, playerName);
        gameRepo.save(availableGame);

        GameJoinResult gameJoinResult = new GameJoinResult(availableGame.getTableId(), availableGame.getPlayerInfoList());

        sseService.sendMessage(gameJoinPresenter.broadcastEvent(playerId, gameJoinResult));

        return gameJoinPresenter.response(playerId, gameJoinResult);
    }
}
