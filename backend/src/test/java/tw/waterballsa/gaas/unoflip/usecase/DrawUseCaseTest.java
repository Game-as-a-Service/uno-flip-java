package tw.waterballsa.gaas.unoflip.usecase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.presenter.DrawCardPresenter;
import tw.waterballsa.gaas.unoflip.presenter.StatusCode;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrawUseCaseTest {
    @Mock
    private GameRepo gameRepo;
    @Mock
    private DrawCardPresenter drawCardPresenter;
    @Mock
    private SseService sseService;
    @Mock
    private UnoFlipGame game;
    @Mock
    private DrawCardResult drawCardResult;

    @InjectMocks
    private DrawUseCase sut;
    private DrawResult drawResult;


    @Test
    void game_not_exists() {
        given_game_not_exists();

        when_draw_then_throw_exception();
    }

    @Test
    void draw_successfully() {
        given_game_exists();
        given_draw_success();
        Response<DrawResult> expected = given_presenter_response();

        Assertions.assertThat(sut.draw(1234, "actionPlayerId")).isEqualTo(expected);
    }

    @Test
    void should_save_game_after_draw() {
        given_game_exists();
        given_draw_success();
        given_presenter_response();

        sut.draw(1234, "actionPlayerId");

        InOrder inOrder = inOrder(game, gameRepo);
        inOrder.verify(game).draw("actionPlayerId");
        inOrder.verify(gameRepo).save(game);
    }

    @Test
    void should_send_broadcast_event() {
        given_game_exists();
        given_draw_success();
        given_presenter_response();
        BroadcastEvent broadcastEvent = new BroadcastEvent(Arrays.asList("player1", "player2", "player3", "player4"), drawResult);
        when(drawCardPresenter.broadcastEvent(drawCardResult)).thenReturn(broadcastEvent);

        sut.draw(1234, "actionPlayerId");

        verify(sseService).sendMessage(broadcastEvent);
    }

    private Response<DrawResult> given_presenter_response() {
        drawResult = new DrawResult(1);
        Response<DrawResult> expected = new Response<>(StatusCode.OK.getCode(), "draw successfully", drawResult);
        when(drawCardPresenter.response(drawCardResult)).thenReturn(expected);
        return expected;
    }

    private void given_draw_success() {
        when(game.draw("actionPlayerId")).thenReturn(drawCardResult);
    }

    private void given_game_exists() {
        when(gameRepo.get(1234)).thenReturn(Optional.of(game));
    }

    private void when_draw_then_throw_exception() {
        Assertions.assertThatThrownBy(() -> sut.draw(1234, "actionPlayerId"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("game 1234 is not exists");
    }

    private void given_game_not_exists() {
        when(gameRepo.get(1234)).thenReturn(Optional.empty());
    }
}