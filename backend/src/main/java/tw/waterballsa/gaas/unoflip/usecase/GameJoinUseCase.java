package tw.waterballsa.gaas.unoflip.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;

@Service
@RequiredArgsConstructor
public class GameJoinUseCase {
    private final GameRepo gameRepo;

    public GameJoinResult join(String playerId, String playerName) {
        UnoFlipGame availableGame = gameRepo.getAvailable().orElseGet(() -> new UnoFlipGame(gameRepo.generateTableId()));
        availableGame.join(playerId, playerName);
        gameRepo.save(availableGame);

        return new GameJoinResult(availableGame.getTableId(), availableGame.getPlayerInfoList());
    }
}
