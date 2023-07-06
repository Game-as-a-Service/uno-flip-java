package tw.waterballsa.gaas.unoflip.service;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameUseCase {
    private final GameRepo gameRepo;

    public GameUseCase(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    public JoinResult join(String playerId, String playerName) {
        UnoFlipGame availableGame = gameRepo.getAvailableGame();
        availableGame.join(playerId, playerName);
        return new JoinResult(1, availableGame.getPosition(playerId), getOtherPlayerInfoList(playerId, availableGame));
    }

    private List<PlayerInfo> getOtherPlayerInfoList(String playerId, UnoFlipGame availableGame) {
        return availableGame.getPlayerInfoList().stream()
                .filter(playerInfo -> !playerId.equals(playerInfo.playerId()))
                .collect(Collectors.toList());
    }
}
