package tw.waterballsa.gaas.unoflip.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;

import static org.assertj.core.api.Assertions.*;

class UnoFlipGameTest {

    private UnoFlipGame sut;

    @BeforeEach
    void setUp() {
        sut = new UnoFlipGame(1);
    }

    @Test
    void get_table_id() {
        UnoFlipGame game = new UnoFlipGame(123);

        Assertions.assertThat(game.getTableId()).isEqualTo(123);
    }

    @Test
    void should_throw_exception_when_join_same_player() {
        sut.join("playerId", "playerName");

        Assertions.assertThatThrownBy(() -> sut.join("playerId", "playerName"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("player already in game");
    }

    @Test
    void join_when_game_is_not_full() {
        sut.join("playerId", "playerName");

        assertThat(sut.getPlayerInfoList()).containsExactly(new PlayerInfo("playerId", "playerName", 1));
    }

    @Test
    void join_when_game_is_full() {
        given_already_four_players_in_the_game();

        Assertions.assertThatThrownBy(() -> sut.join("playerId5", "player5"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("game is full");
    }

    @Test
    void game_is_full() {
        given_already_four_players_in_the_game();

        Assertions.assertThat(sut.isFull()).isTrue();
    }

    private void given_already_four_players_in_the_game() {
        sut.join("playerId1", "player1");
        sut.join("playerId2", "player2");
        sut.join("playerId3", "player3");
        sut.join("playerId4", "player4");
    }
}