package tw.waterballsa.gaas.unoflip.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameUseCaseTest {
    @Mock
    private GameRepo gameRepo;
    @Mock
    private UnoFlipGame unoFlipGame;
    private GameUseCase sut;
    private List<PlayerInfo> shadowJoinPlayerInfoList;
    private List<PlayerInfo> maxJoinPlayerInfoList;

    @BeforeEach
    void setUp() {
        sut = new GameUseCase(gameRepo);
    }

    @Test
    void join_success() {
        given_shadow_is_the_first_player_of_the_game();
        given_max_is_the_second_player_of_the_game();
        init_game();

        assertThat(sut.join("shadow#123", "Shadow")).isEqualTo(new JoinResult(1, 1, shadowJoinPlayerInfoList));
        assertThat(sut.join("max#456", "Max")).isEqualTo(new JoinResult(1, 2, maxJoinPlayerInfoList));
    }

    private void given_max_is_the_second_player_of_the_game() {
        given_game_position_for("max#456", 2);
        maxJoinPlayerInfoList = Collections.singletonList(new PlayerInfo("shadow#123", "Shadow", 1));
    }

    private void given_shadow_is_the_first_player_of_the_game() {
        given_game_position_for("shadow#123", 1);
        shadowJoinPlayerInfoList = Collections.emptyList();
    }

    private void given_game_position_for(String playerId, int position) {
        when(unoFlipGame.getPosition(playerId)).thenReturn(position);
    }

    private void init_game() {
        when(unoFlipGame.getPlayerInfoList()).thenReturn(shadowJoinPlayerInfoList, maxJoinPlayerInfoList);
        when(gameRepo.getAvailableGame()).thenReturn(unoFlipGame);
    }
}