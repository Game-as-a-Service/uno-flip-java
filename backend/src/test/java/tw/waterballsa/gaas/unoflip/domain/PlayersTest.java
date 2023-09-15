package tw.waterballsa.gaas.unoflip.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        HandCard handCard = given_hand_card_for_player1111_in_player_list();

        Assertions.assertThat(sut.getPlayerHandCard("player1111")).isEqualTo(handCard);
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
    void get_player_ids() {
        given_player1111_in_player_list();

        Assertions.assertThat(sut.getIds())
                .hasSize(1)
                .containsExactly("player1111");
    }

    @Test
    void get_player_id() {
        given_player_list_has_player_info("player1111", 1);
        given_player_list_has_player_info("player2222", 2);
        given_player_list_has_player_info("player3333", 3);

        Assertions.assertThat(sut.getPlayerId(2)).isEqualTo("player2222");
    }

    @Test
    void position_not_exists() {
        Assertions.assertThatThrownBy(() -> sut.getPlayerId(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private HandCard given_hand_card_for_player1111_in_player_list() {
        given_player1111_in_player_list();
        HandCard handCard = new HandCard(Collections.emptyList());

        sut.setHandCard("player1111", handCard);

        return handCard;
    }

    private void given_player_list_has_player_info(String playerId, int position) {
        PlayerInfo playerInfo = new PlayerInfo(playerId, "NO_CARE", position);

        sut.add(playerInfo);
    }


    private void given_player1111_in_player_list() {
        PlayerInfo playerInfo = given_player_info("player1111", "NO_CARE");

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