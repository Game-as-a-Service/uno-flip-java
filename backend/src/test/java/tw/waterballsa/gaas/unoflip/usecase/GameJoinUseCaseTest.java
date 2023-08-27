package tw.waterballsa.gaas.unoflip.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.presenter.GameJoinPresenter;
import tw.waterballsa.gaas.unoflip.presenter.StatusCode;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameJoinUseCaseTest {
    private static final String MAX_PLAYER_ID = "max456";
    private static final String SHADOW_PLAYER_ID = "shadow123";

    @Mock
    private GameRepo gameRepo;
    @Mock
    private SseService sseService;
    @Mock
    private GameJoinPresenter gameJoinPresenter;
    @Mock
    private UnoFlipGame unoFlipGame;
    @Mock
    private UnoFlipGame unoFlipGame1;
    @Mock
    private UnoFlipGame unoFlipGame2;

    private List<PlayerInfo> playerInfoList;
    private List<PlayerInfo> playerInfoList1;
    private List<PlayerInfo> playerInfoList2;
    private Response<JoinResult> joinTableResponse;
    private Response<JoinResult> joinTable1Response;
    private Response<JoinResult> joinTable2Response;
    private GameJoinUseCase sut;


    @BeforeEach
    void setUp() {
        sut = new GameJoinUseCase(gameRepo, gameJoinPresenter, sseService);
    }

    @Test
    void should_create_new_game_when_no_available_game() {
        given_no_available_game();
        given_new_game_table_id_is_123();

        when_someone_join();

        then_table_id_is_123();
    }

    @Test
    void should_save_game() {
        when(gameRepo.getAvailable()).thenReturn(Optional.of(unoFlipGame));

        when_shadow_join();

        verify(gameRepo).save(unoFlipGame);
    }

    @Test
    void should_send_event() {
        BroadcastEvent event = given_broadcast_event();

        when_shadow_join();

        verify(sseService).sendMessage(event);
    }

    @Test
    void two_player_join_different_game() {
        init_two_games();
        given_shadow_join_table_1();
        given_max_join_table_2();

        when_shadow_join();
        then_shadow_should_at_position_1();

        when_max_join();
        then_max_should_at_position(1);

        then_shadow_and_max_should_in_different_game();
    }

    @Test
    void two_player_join_the_same_game() {
        init_game();
        given_shadow_is_the_first_player_of_the_game();
        given_max_is_the_second_player_of_the_game();

        when_shadow_join();
        then_shadow_should_at_position_1();

        when_max_join();
        then_max_should_at_position(2);

        then_shadow_and_max_should_in_the_same_game();
    }

    private void when_someone_join() {
        joinTableResponse = sut.join("SOMEONE", "NO_CARE");
    }

    private void init_game() {
        playerInfoList = new ArrayList<>();
        when(unoFlipGame.getPlayerInfoList()).thenReturn(playerInfoList);
        lenient().when(unoFlipGame.getTableId()).thenReturn(1);
        when(gameRepo.getAvailable()).thenReturn(Optional.of(unoFlipGame));
    }

    private void init_two_games() {
        playerInfoList1 = new ArrayList<>();
        when(unoFlipGame1.getPlayerInfoList()).thenReturn(playerInfoList1);
        when(unoFlipGame1.getTableId()).thenReturn(1);

        playerInfoList2 = new ArrayList<>();
        when(unoFlipGame2.getPlayerInfoList()).thenReturn(playerInfoList2);
        when(unoFlipGame2.getTableId()).thenReturn(2);

        doReturn(Optional.of(unoFlipGame1), Optional.of(unoFlipGame2)).when(gameRepo).getAvailable();
    }

    private void given_no_available_game() {
        when(gameRepo.getAvailable()).thenReturn(Optional.empty());
    }

    private void given_shadow_is_the_first_player_of_the_game() {
        Response<JoinResult> response = given_response(1, 1);
        when(gameJoinPresenter.response(SHADOW_PLAYER_ID, new GameJoinResult(1, playerInfoList))).thenReturn(response);
    }

    private void given_max_is_the_second_player_of_the_game() {
        Response<JoinResult> response = given_response(1, 2);
        when(gameJoinPresenter.response(MAX_PLAYER_ID, new GameJoinResult(1, playerInfoList))).thenReturn(response);
    }

    private void given_shadow_join_table_1() {
        Response<JoinResult> response = given_response(1, 1);
        when(gameJoinPresenter.response(SHADOW_PLAYER_ID, new GameJoinResult(1, playerInfoList1))).thenReturn(response);
    }

    private void given_max_join_table_2() {
        Response<JoinResult> response = given_response(2, 1);
        when(gameJoinPresenter.response(MAX_PLAYER_ID, new GameJoinResult(2, playerInfoList2))).thenReturn(response);
    }

    private Response<JoinResult> given_response(int tableId, int position) {
        JoinResult joinResult = given_join_result(tableId, position);
        return new Response<>(StatusCode.OK.getCode(), "join successfully", joinResult);
    }

    private void given_new_game_table_id_is_123() {
        when(gameRepo.generateTableId()).thenReturn(123);

        JoinResult joinResult = mock(JoinResult.class);
        when(joinResult.tableId()).thenReturn(123);
        Response<JoinResult> response = new Response<>(StatusCode.OK.getCode(), "join successfully", joinResult);
        when(gameJoinPresenter.response(eq("SOMEONE"), isA(GameJoinResult.class))).thenReturn(response);
    }

    private JoinResult given_join_result(int tableId, int position) {
        JoinResult joinResult = mock(JoinResult.class);
        when(joinResult.tableId()).thenReturn(tableId);
        when(joinResult.position()).thenReturn(position);
        return joinResult;
    }

    private BroadcastEvent given_broadcast_event() {
        BroadcastEvent event = mock(BroadcastEvent.class);
        when(gameJoinPresenter.broadcastEvent(eq(SHADOW_PLAYER_ID), isA(GameJoinResult.class))).thenReturn(event);
        return event;
    }

    private void when_shadow_join() {
        joinTable1Response = sut.join(SHADOW_PLAYER_ID, "Shadow");
    }

    private void when_max_join() {
        joinTable2Response = sut.join(MAX_PLAYER_ID, "Max");
    }

    private void then_table_id_is_123() {
        assertThat(joinTableResponse.payload().tableId()).isEqualTo(123);
    }

    private void then_shadow_should_at_position_1() {
        assertThat(joinTable1Response.payload().position()).isEqualTo(1);
    }

    private void then_max_should_at_position(int exceptedPosition) {
        assertThat(joinTable2Response.payload().position()).isEqualTo(exceptedPosition);
    }

    private void then_shadow_and_max_should_in_different_game() {
        assertThat(joinTable1Response.payload().tableId()).isNotEqualTo(joinTable2Response.payload().tableId());
    }

    private void then_shadow_and_max_should_in_the_same_game() {
        assertThat(joinTable1Response.payload().tableId()).isEqualTo(joinTable2Response.payload().tableId());
    }
}