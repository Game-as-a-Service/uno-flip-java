package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.Collections;
import java.util.List;

public class HandCard {
    private final List<Card> cards;

    public HandCard(List<Card> cards) {
        this.cards = cards;
    }

    public List<Integer> toCardIds() {
        return cards.stream().map(Card::getId).toList();
    }

    int size() {
        return cards.size();
    }

    List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
