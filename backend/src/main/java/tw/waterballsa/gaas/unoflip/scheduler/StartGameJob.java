package tw.waterballsa.gaas.unoflip.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.usecase.GameStartUseCase;

@Service
@RequiredArgsConstructor
public class StartGameJob {

    private final GameStartUseCase gameStartUseCase;


    @Scheduled(fixedDelay = 1000)
    public void tryStartGame() {
        gameStartUseCase.tryStartGame();
    }
}


