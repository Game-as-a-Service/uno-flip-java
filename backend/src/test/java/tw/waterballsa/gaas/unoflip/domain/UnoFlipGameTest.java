package tw.waterballsa.gaas.unoflip.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.eumns.GameStatus;

import static org.assertj.core.api.Assertions.assertThat;

class UnoFlipGameTest {

    private UnoFlipGame sut;

    @BeforeEach
    void setUp() {
        sut = new UnoFlipGame(1);
    }

    @Test
    void get_table_id() {
        Assertions.assertThat(new UnoFlipGame(123).getTableId()).isEqualTo(123);
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

    @Test
    void init_game_status() {
        assertThat(new UnoFlipGame(123).getStatus()).isEqualTo(GameStatus.WAITING);
    }

    @Test
    void start_game_when_status_is_waiting() {
        given_already_four_players_in_the_game();

        sut.start();

        then_game_status_should_be_started();
        then_should_assign_init_player();
        then_each_player_should_has_7_cards();
        then_discard_pile_should_has_one_card();
        then_draw_pile_should_has_83_cards();
    }

    private void then_should_assign_init_player() {
        assertThat(sut.getActionPlayerId()).isNotEmpty();
    }

    private void then_discard_pile_should_has_one_card() {
        assertThat(sut.getDiscardPile()).hasSize(1);
    }

    private void then_draw_pile_should_has_83_cards() {
        assertThat(sut.getDrawPile()).hasSize(83);
    }

    private void then_game_status_should_be_started() {
        assertThat(sut.getStatus()).isEqualTo(GameStatus.STARTED);
    }

    private void then_each_player_should_has_7_cards() {
        Players players = sut.getPlayers();
        assertThat(players.getPlayerHandCard("player1111").size()).isEqualTo(7);
        assertThat(players.getPlayerHandCard("player2222").size()).isEqualTo(7);
        assertThat(players.getPlayerHandCard("player3333").size()).isEqualTo(7);
        assertThat(players.getPlayerHandCard("player4444").size()).isEqualTo(7);
    }

    private void given_already_four_players_in_the_game() {
        sut.join("player1111", "playerA");
        sut.join("player2222", "playerB");
        sut.join("player3333", "playerC");
        sut.join("player4444", "playerD");
    }

}