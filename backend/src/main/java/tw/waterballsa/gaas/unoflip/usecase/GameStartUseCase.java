package tw.waterballsa.gaas.unoflip.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.presenter.GameStartPresenter;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.GameStartResult;
import tw.waterballsa.gaas.unoflip.event.PersonalEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameStartUseCase {
    private final GameRepo gameRepo;
    private final SseService sseService;
    private final GameStartPresenter gameStartPresenter;

    public void tryStartGame() {
        for (UnoFlipGame waitingGame : gameRepo.getWaitingGames()) {
            if (!waitingGame.isFull()) {
                continue;
            }

            waitingGame.start();
            gameRepo.save(waitingGame);

            GameStartResult gameStartResult = new GameStartResult(
                    waitingGame.getActionPlayerId(),
                    waitingGame.getDirection(),
                    waitingGame.getPlayers(),
                    waitingGame.getDiscardPile(),
                    waitingGame.getDrawPile());

            sseService.sendMessage(gameStartPresenter.broadcastEvent(gameStartResult));

            List<PersonalEvent> personalEvents = gameStartPresenter.personalEvents(gameStartResult);
            personalEvents.forEach(sseService::sendMessage);
        }
    }
}
