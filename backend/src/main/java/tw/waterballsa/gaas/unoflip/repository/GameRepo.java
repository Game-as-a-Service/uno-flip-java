package tw.waterballsa.gaas.unoflip.repository;

import org.springframework.stereotype.Repository;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;

@Repository
public class GameRepo {
    public UnoFlipGame getAvailableGame() {
        return new UnoFlipGame(1);
    }

    public void saveGame(UnoFlipGame availableGame) {

    }
}
