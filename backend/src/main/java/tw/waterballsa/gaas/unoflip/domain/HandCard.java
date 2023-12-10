package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandCard {
    private final List<Card> cards;

    public HandCard(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
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

    public void add(Card card) {
        cards.add(card);
    }
}
