package tw.waterballsa.gaas.unoflip.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.Collections;

class PlayersTest {

    private Players sut;

    @BeforeEach
    void setUp() {
        sut = new Players();
    }

    @Test
    void add() {
        PlayerInfo playerInfo = given_player_info("player1111", "playerA");

        sut.add(playerInfo);

        Assertions.assertThat(sut.exists("player1111")).isTrue();
    }

    @Test
    void get_player_info_list() {
        PlayerInfo playerInfo = given_player_list_has_one_player_info();

        Assertions.assertThat(sut.toInfoList())
                .hasSize(1)
                .containsExactly(playerInfo);
    }

    @Test
    void get_player_hand_card() {
        HandCard handCard = given_hand_card_for_shadow();

        Assertions.assertThat(sut.getPlayerHandCard("ShadowId")).isEqualTo(handCard);
    }

    @Test
    void player_hand_card_not_exists() {
        Assertions.assertThatThrownBy(() -> sut.getPlayerHandCard("notExistsPlayerId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void size() {
        given_player_list_has_one_player_info();

        Assertions.assertThat(sut.size()).isEqualTo(1);
    }

    @Test
    void get_all_player_id() {
        given_player_list_has_player("ShadowId", 1);
        given_player_list_has_player("MaxId", 2);

        Assertions.assertThat(sut.getIds())
                .hasSize(2)
                .containsSequence("ShadowId", "MaxId");
    }

    @Test
    void get_player_id() {
        given_player_list_has_player("player1111", 1);
        given_player_list_has_player("player2222", 2);
        given_player_list_has_player("player3333", 3);

        Assertions.assertThat(sut.getPlayerId(2)).isEqualTo("player2222");
    }

    @Test
    void position_not_exists_when_get_player_id() {
        Assertions.assertThatThrownBy(() -> sut.getPlayerId(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void get_player_position() {
        given_player_list_has_player("player1111", 1);
        given_player_list_has_player("player2222", 2);

        Assertions.assertThat(sut.getPlayerPosition("player2222")).isEqualTo(2);
    }

    @Test
    void player_id_not_exists_when_get_player_position() {
        Assertions.assertThatThrownBy(() -> sut.getPlayerPosition("ShadowId"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("player ShadowId not exists");
    }

    @Test
    void add_player_hand_card() {
        given_player_list_has_player("player1111", 1);
        given_player1111_has_no_card();

        sut.addPlayerHandCard("player1111", Card.CARD_3);

        Assertions.assertThat(sut.getPlayerHandCard("player1111").getCards()).containsExactly(Card.CARD_3);
    }

    @Test
    void player_id_not_exists_when_add_player_hand_card() {
        Assertions.assertThatThrownBy(() -> sut.addPlayerHandCard("ShadowId", Card.CARD_5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("player ShadowId not exists");
    }

    private void given_player1111_has_no_card() {
        sut.setHandCard("player1111", new HandCard(Collections.emptyList()));
    }

    private HandCard given_hand_card_for_shadow() {
        given_player_list_has_player("ShadowId", 1);
        HandCard handCard = new HandCard(Collections.emptyList());

        sut.setHandCard("ShadowId", handCard);

        return handCard;
    }

    private void given_player_list_has_player(String playerId, int position) {
        PlayerInfo playerInfo = new PlayerInfo(playerId, "NO_CARE", position);

        sut.add(playerInfo);
    }

    private PlayerInfo given_player_list_has_one_player_info() {
        PlayerInfo playerInfo = given_player_info("NO_CARE", "NO_CARE");

        sut.add(playerInfo);

        return playerInfo;
    }

    private PlayerInfo given_player_info(String playerId, String playerName) {
        return new PlayerInfo(playerId, playerName, 1);
    }
}