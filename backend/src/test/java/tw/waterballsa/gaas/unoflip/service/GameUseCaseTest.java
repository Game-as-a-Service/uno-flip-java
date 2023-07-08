package tw.waterballsa.gaas.unoflip.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameUseCaseTest {
    private static final String MAX_PLAYER_ID = "max456";
    private static final String SHADOW_PLAYER_ID = "shadow123";
    private final String MAX_NAME = "Max";
    private final String SHADOW_NAME = "Shadow";

    @Mock
    private GameRepo gameRepo;
    @Mock
    private UnoFlipGame unoFlipGame;
    @Mock
    private UnoFlipGame unoFlipGame1;
    @Mock
    private UnoFlipGame unoFlipGame2;
    private GameUseCase sut;
    private List<PlayerInfo> playerInfoList;
    private GameJoinResult shadowJoinResult;
    private GameJoinResult maxJoinResult;
    private List<PlayerInfo> playerInfoList1;

    private List<PlayerInfo> playerInfoList2;

    @BeforeEach
    void setUp() {
        sut = new GameUseCase(gameRepo);
    }

    @Test
    void should_create_new_game_when_no_available_game() {
        given_no_available_game();
        given_next_table_id_is_123();

        when_join_game_then_table_id_is_123();
    }

    @Test
    void should_save_game() {
        when(gameRepo.getAvailable()).thenReturn(Optional.of(unoFlipGame));

        sut.join(SHADOW_PLAYER_ID, SHADOW_NAME);

        verify(gameRepo).save(unoFlipGame);
    }

    @Test
    void two_player_join_different_game() {
        init_two_games();
        given_shadow_join_table_1();
        given_max_join_table_2();

        when_shadow_join();
        then_shadow_should_at_position(1);

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
        then_shadow_should_at_position(1);

        when_max_join();
        then_max_should_at_position(2);

        then_shadow_and_max_should_in_the_same_game();
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
        playerInfoList.add(new PlayerInfo(SHADOW_PLAYER_ID, SHADOW_NAME, 1));
    }

    private void given_max_is_the_second_player_of_the_game() {
        playerInfoList.add(new PlayerInfo(MAX_PLAYER_ID, MAX_NAME, 2));
    }

    private void given_shadow_join_table_1() {
        playerInfoList1.add(new PlayerInfo(SHADOW_PLAYER_ID, SHADOW_NAME, 1));
    }

    private void given_next_table_id_is_123() {
        when(gameRepo.generateTableId()).thenReturn(123);
    }

    private void given_max_join_table_2() {
        playerInfoList2.add(new PlayerInfo(MAX_PLAYER_ID, MAX_NAME, 1));
    }

    private void when_join_game_then_table_id_is_123() {
        assertThat(sut.join(SHADOW_PLAYER_ID, SHADOW_NAME).tableId()).isEqualTo(123);
    }

    private void when_shadow_join() {
        shadowJoinResult = sut.join(SHADOW_PLAYER_ID, SHADOW_NAME);
    }

    private void when_max_join() {
        maxJoinResult = sut.join(MAX_PLAYER_ID, MAX_NAME);
    }

    private void then_shadow_should_at_position(int expectedPosition) {
        Integer shadowPosition = shadowJoinResult.playerInfoList().stream()
                .filter(playerInfo -> SHADOW_PLAYER_ID.equals(playerInfo.playerId()))
                .map(PlayerInfo::position)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("player %s not int game".formatted(SHADOW_PLAYER_ID)));
        assertThat(shadowPosition).isEqualTo(expectedPosition);
    }

    private void then_shadow_and_max_should_in_different_game() {
        assertThat(shadowJoinResult.tableId()).isNotEqualTo(maxJoinResult.tableId());
    }

    private void then_shadow_and_max_should_in_the_same_game() {
        assertThat(shadowJoinResult.tableId()).isEqualTo(maxJoinResult.tableId());
    }

    private void then_max_should_at_position(int exceptedPosition) {
        Integer maxPosition = maxJoinResult.playerInfoList().stream()
                .filter(playerInfo -> MAX_PLAYER_ID.equals(playerInfo.playerId()))
                .map(PlayerInfo::position)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("player %s not int game".formatted(MAX_PLAYER_ID)));
        assertThat(maxPosition).isEqualTo(exceptedPosition);
    }
}