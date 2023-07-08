package tw.waterballsa.gaas.unoflip.repository;

import org.springframework.stereotype.Repository;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;

import java.util.Optional;

@Repository
public class GameRepo {
    public Optional<UnoFlipGame> getAvailableGame() {
        return Optional.empty();
    }

    public void saveGame(UnoFlipGame availableGame) {

    }

    public int generateTableId() {
        return 0;
    }
}
