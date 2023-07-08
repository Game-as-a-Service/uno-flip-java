package tw.waterballsa.gaas.unoflip.service;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;

@Service
public class GameUseCase {
    private final GameRepo gameRepo;

    public GameUseCase(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    public GameJoinResult join(String playerId, String playerName) {
        UnoFlipGame availableGame = gameRepo.getAvailable().orElseGet(() -> new UnoFlipGame(gameRepo.generateTableId()));
        availableGame.join(playerId, playerName);
        gameRepo.save(availableGame);

        return new GameJoinResult(availableGame.getTableId(), availableGame.getPlayerInfoList());
    }
}
