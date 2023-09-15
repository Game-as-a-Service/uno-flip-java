package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

class Dealer {
    public static DealResult deal() {
        List<Integer> cardNumbers = Card.getAllIds();
        Collections.shuffle(cardNumbers);

        List<HandCard> playersHandCards = new ArrayList<>();

        playersHandCards.add(createHandCard(0, 7, cardNumbers));
        playersHandCards.add(createHandCard(7, 14, cardNumbers));
        playersHandCards.add(createHandCard(14, 21, cardNumbers));
        playersHandCards.add(createHandCard(21, 28, cardNumbers));

        Card discardCard = Card.getLightInstance(cardNumbers.get(28));

        List<Card> drawPileCards = getRandomCardList(29, 112, cardNumbers);

        return new DealResult(playersHandCards, discardCard, drawPileCards);
    }

    private static HandCard createHandCard(int startInclusive, int endExclusive, List<Integer> cardNumbers) {
        return new HandCard(getRandomCardList(startInclusive, endExclusive, cardNumbers));
    }

    private static List<Card> getRandomCardList(int startInclusive, int endExclusive, List<Integer> cardNumbers) {
        return IntStream.range(startInclusive, endExclusive).mapToObj(i -> Card.getLightInstance(cardNumbers.get(i))).toList();
    }
}
