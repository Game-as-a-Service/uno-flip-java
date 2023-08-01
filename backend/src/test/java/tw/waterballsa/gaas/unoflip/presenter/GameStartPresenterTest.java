package tw.waterballsa.gaas.unoflip.presenter;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.HandCard;
import tw.waterballsa.gaas.unoflip.domain.Players;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.domain.eumns.Direction;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.PersonalEvent;
import tw.waterballsa.gaas.unoflip.event.StartedBroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.StartedPersonalEvent;
import tw.waterballsa.gaas.unoflip.vo.*;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameStartPresenterTest {

    private GameStartPresenter sut;

    @BeforeEach
    void setUp() {
        sut = new GameStartPresenter();
    }

    @Test
    void broadcast_event() {
        GameStartResult gameStartResult = new GameStartResult(
                "player1111",
                Direction.RIGHT,
                given_players("player1111", "player2222", "player3333", "player4444"),
                Collections.singletonList(Card.CARD_1),
                Collections.singletonList(Card.CARD_2));

        Assertions.assertThat(sut.broadcastEvent(gameStartResult)).isEqualTo(
                new BroadcastEvent(ImmutableList.of("player1111", "player2222", "player3333", "player4444"),
                        new StartedBroadcastEvent(
                                EventType.STARTED.getCode(),
                                "player1111",
                                Direction.RIGHT.getCode(),
                                Card.CARD_1.getId(),
                                Collections.singletonList(Card.CARD_2.getId()))));
    }

    @Test
    void personal_events() {
        Players players = given_players_are("player1111", "player2222", "player3333", "player4444");
        HandCard handCardForPlayer1111 = given_player_hand_card(players, "player1111", Card.CARD_1);
        HandCard handCardForPlayer2222 = given_player_hand_card(players, "player2222", Card.CARD_2);
        HandCard handCardForPlayer3333 = given_player_hand_card(players, "player3333", Card.CARD_3);
        HandCard handCardForPlayer4444 = given_player_hand_card(players, "player4444", Card.CARD_4);

        GameStartResult gameStartResult = new GameStartResult("NO_CARE",
                Direction.RIGHT,
                players,
                Collections.emptyList(),
                Collections.emptyList());

        Assertions.assertThat(sut.personalEvents(gameStartResult)).containsExactly(
                new PersonalEvent("player1111", new StartedPersonalEvent(EventType.HAND_CARD.getCode(), handCardForPlayer1111.toCardIds())),
                new PersonalEvent("player2222", new StartedPersonalEvent(EventType.HAND_CARD.getCode(), handCardForPlayer2222.toCardIds())),
                new PersonalEvent("player3333", new StartedPersonalEvent(EventType.HAND_CARD.getCode(), handCardForPlayer3333.toCardIds())),
                new PersonalEvent("player4444", new StartedPersonalEvent(EventType.HAND_CARD.getCode(), handCardForPlayer4444.toCardIds())));
    }

    private Players given_players_are(String... playerIds) {
        Players players = mock(Players.class);
        when(players.getIds()).thenReturn(Arrays.asList(playerIds));
        return players;
    }

    private HandCard given_player_hand_card(Players players, String playerId, Card card) {
        HandCard handCard = given_hand_card_for_player(card);
        when(players.getPlayerHandCard(playerId)).thenReturn(handCard);
        return handCard;
    }

    private HandCard given_hand_card_for_player(Card card) {
        return new HandCard(Collections.singletonList(card));
    }

    private Players given_players(String... playerIds) {
        Players players = mock(Players.class);
        when(players.getIds()).thenReturn(Arrays.asList(playerIds));
        return players;
    }

}