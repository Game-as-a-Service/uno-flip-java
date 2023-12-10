package tw.waterballsa.gaas.unoflip.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.domain.eumns.GameStatus;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

import java.util.Arrays;

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
        sut.join("actionPlayerId", "playerName");

        Assertions.assertThatThrownBy(() -> sut.join("actionPlayerId", "playerName"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("player already in game");
    }

    @Test
    void join_when_game_is_not_full() {
        sut.join("actionPlayerId", "playerName");

        assertThat(sut.getPlayerInfoList()).containsExactly(new PlayerInfo("actionPlayerId", "playerName", 1));
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

    @Test
    void draw_fail_when_game_not_started() {
        sut.join("ShadowId", "Shadow");

        Assertions.assertThatThrownBy(() -> sut.draw("ShadowId"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Game is not started");
    }

    @Test
    void shadow_draw_fail_when_action_player_is_not_shadow() {
        given_game_has_player_shadow_max_hannah_archie();
        given_game_started();
        given_now_is_max_turn();

        Assertions.assertThatThrownBy(() -> sut.draw("ShadowId"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("It is not ShadowId turn");
    }

    @Test
    void draw_success_if_is_shadow_turn() {
        given_game_has_player_shadow_max_hannah_archie();
        given_game_started();
        given_now_is_shadow_turn();
        Card topCardInDrawPile = get_current_top_card_in_draw_pile();

        when_draw_then_success(topCardInDrawPile);
        then_shadow_hand_card_has_draw_card(topCardInDrawPile);
    }

    private void then_shadow_hand_card_has_draw_card(Card topCardInDrawPile) {
        HandCard shadowHandCard = sut.getPlayers().getPlayerHandCard("ShadowId");
        Assertions.assertThat(shadowHandCard.getCards()).contains(topCardInDrawPile);
    }

    private void when_draw_then_success(Card topCardInDrawPile) {
        DrawCardResult expected = new DrawCardResult("ShadowId", topCardInDrawPile, "MaxId",
                Arrays.asList("ShadowId", "MaxId", "HannahId", "ArchieId"));

        assertThat(sut.draw("ShadowId")).usingRecursiveComparison().isEqualTo(expected);
    }

    private void given_game_started() {
        sut.start();
    }

    private Card get_current_top_card_in_draw_pile() {
        return sut.getTopDrawCard();
    }

    private void given_now_is_shadow_turn() {
        sut.setActionPosition(1);
    }

    private void given_now_is_max_turn() {
        sut.setActionPosition(2);
    }

    private void given_game_has_player_shadow_max_hannah_archie() {
        sut.join("ShadowId", "Shadow");
        sut.join("MaxId", "Max");
        sut.join("HannahId", "Hannah");
        sut.join("ArchieId", "Archie");
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