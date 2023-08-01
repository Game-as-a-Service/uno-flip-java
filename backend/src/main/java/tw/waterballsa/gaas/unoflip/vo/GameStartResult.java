package tw.waterballsa.gaas.unoflip.vo;

import tw.waterballsa.gaas.unoflip.domain.Players;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.domain.eumns.Direction;

import java.util.List;

public record GameStartResult(String currentPlayerId, Direction direction, Players players,
                              List<Card> discardPileCards, List<Card> drawPileCards) {
}
