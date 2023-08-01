package tw.waterballsa.gaas.unoflip.usecase;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.presenter.GameStartPresenter;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.vo.GameStartResult;
import tw.waterballsa.gaas.unoflip.event.PersonalEvent;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameStartUseCaseTest {

    @Mock
    private GameRepo gameRepo;
    @Mock
    private UnoFlipGame game;
    @Mock
    private SseService sseService;
    @Mock
    private GameStartPresenter gameStartPresenter;
    @Mock
    private BroadcastEvent broadcastEvent;

    private GameStartUseCase sut;

    @BeforeEach
    void setUp() {
        sut = new GameStartUseCase(gameRepo, sseService, gameStartPresenter);
    }

    @Test
    void start_game_when_player_is_full() {
        given_game_full(true);
        given_presenter_present_broadcast_event();
        given_presenter_present_personal_events();

        when_try_start_game();

        should_start_game();
        should_save_into_repo();
        should_send_broadcast_event();
        should_send_personal_events();
    }

    private void given_presenter_present_personal_events() {
        when(gameStartPresenter.personalEvents(isA(GameStartResult.class))).thenReturn(ImmutableList.of(
                mock(PersonalEvent.class),
                mock(PersonalEvent.class),
                mock(PersonalEvent.class),
                mock(PersonalEvent.class)));
    }

    private void should_send_personal_events() {
        verify(sseService, times(4)).sendMessage(isA(PersonalEvent.class));
    }

    @Test
    void should_not_start_game_when_player_is_not_full() {
        given_game_full(false);

        when_try_start_game();

        should_not_start_game();
        should_not_save_into_repo();
    }

    @Test
    void should_check_all_waiting_games() {
        UnoFlipGame game1 = mock(UnoFlipGame.class);
        UnoFlipGame game2 = mock(UnoFlipGame.class);

        given_waiting_games(game1, game2);

        when_try_start_game();

        should_check(game1, game2);
    }

    private void should_send_broadcast_event() {
        verify(sseService).sendMessage(broadcastEvent);
    }

    private void should_check(UnoFlipGame... games) {
        for (UnoFlipGame game : games) {
            verify(game).isFull();
        }
    }

    private void should_not_save_into_repo() {
        verify(gameRepo, never()).save(game);
    }

    private void should_not_start_game() {
        verify(game, never()).start();
    }

    private void should_save_into_repo() {
        verify(gameRepo).save(game);
    }

    private void should_start_game() {
        verify(game).start();
    }

    private void when_try_start_game() {
        sut.tryStartGame();
    }

    private void given_presenter_present_broadcast_event() {
        when(gameStartPresenter.broadcastEvent(isA(GameStartResult.class))).thenReturn(broadcastEvent);
    }

    private void given_game_full(boolean isFull) {
        when(game.isFull()).thenReturn(isFull);
        given_waiting_games(game);
    }

    private void given_waiting_games(UnoFlipGame... games) {
        when(gameRepo.getWaitingGames()).thenReturn(new HashSet<>(Arrays.asList(games)));
    }

}