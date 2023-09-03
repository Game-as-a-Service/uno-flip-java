package tw.waterballsa.gaas.unoflip.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.presenter.GameJoinPresenter;
import tw.waterballsa.gaas.unoflip.presenter.GameStartPresenter;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.GameStartResult;
import tw.waterballsa.gaas.unoflip.response.JoinResult;
import tw.waterballsa.gaas.unoflip.response.Response;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameJoinUseCase {
    private final GameRepo gameRepo;
    private final GameJoinPresenter gameJoinPresenter;
    private final GameStartPresenter gameStartPresenter;
    private final SseService sseService;

    public synchronized Response<JoinResult> join(String playerId, String playerName) {
        UnoFlipGame availableGame = gameRepo.getAvailable().orElseGet(() -> new UnoFlipGame(gameRepo.generateTableId()));
        availableGame.join(playerId, playerName);

        Optional<GameStartResult> gameStartResult = tryStartGame(availableGame);

        gameRepo.save(availableGame);

        GameJoinResult gameJoinResult = new GameJoinResult(availableGame.getTableId(), availableGame.getPlayerInfoList());
        sseService.sendMessage(gameJoinPresenter.broadcastEvent(playerId, gameJoinResult));
        gameStartResult.ifPresent(this::sendStartedEvents);

        return gameJoinPresenter.response(playerId, gameJoinResult);
    }

    private Optional<GameStartResult> tryStartGame(UnoFlipGame availableGame) {
        if (!availableGame.isFull()) {
            return Optional.empty();
        }

        availableGame.start();

        return Optional.of(new GameStartResult(
                availableGame.getActionPlayerId(),
                availableGame.getDirection(),
                availableGame.getPlayers(),
                availableGame.getDiscardPile(),
                availableGame.getDrawPile()));
    }

    private void sendStartedEvents(GameStartResult gameStartResult) {
        sseService.sendMessage(gameStartPresenter.broadcastEvent(gameStartResult));
        gameStartPresenter.personalEvents(gameStartResult).forEach(sseService::sendMessage);
    }
}
