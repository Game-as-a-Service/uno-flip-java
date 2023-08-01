package tw.waterballsa.gaas.unoflip.scheduler;

import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.usecase.GameStartUseCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StartGameJobTest {

    @Test
    void try_start_games() {
        GameStartUseCase gameStartUseCase = mock(GameStartUseCase.class);
        StartGameJob sut = new StartGameJob(gameStartUseCase);

        sut.tryStartGame();

        verify(gameStartUseCase).tryStartGame();
    }


}