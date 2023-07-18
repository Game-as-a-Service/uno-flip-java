package tw.waterballsa.gaas.unoflip.repository;

import org.springframework.stereotype.Repository;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class GameRepo {
    private final AtomicInteger tableIdCounter = new AtomicInteger(1);
    private final Set<UnoFlipGame> games = new HashSet<>();

    public Optional<UnoFlipGame> get(int tableId) {
        return games.stream().filter(game -> tableId == game.getTableId()).findFirst();
    }

    public Optional<UnoFlipGame> getAvailable() {
        return games.stream().filter(game -> !game.isFull()).findFirst();
    }

    public void save(UnoFlipGame game) {
        if (null == game) {
            throw new NullPointerException("Save game failed, game is null.");
        }

        games.add(game);
    }

    public int generateTableId() {
        return tableIdCounter.getAndIncrement();
    }
}
