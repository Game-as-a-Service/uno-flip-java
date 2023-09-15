package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.List;

record DealResult(List<HandCard> playersHandCard, Card discardCard, List<Card> drawPileCards) {
}
