package tw.waterballsa.gaas.unoflip.domain;

import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DealerTest {

    private DealResult dealResult;

    @Test
    void deal() {
        when_deal();

        should_provide_different_hand_cards_for_four_players();
        should_has_discard_card();
        should_has_83_cards_in_draw_pile_cards();
        should_has_total_112_non_duplicate_cards_in_draw_result();
    }

    @Test
    void should_be_different_in_each_deal() {
        DealResult firstDealResult = Dealer.deal();
        DealResult secondDealResult = Dealer.deal();

        should_be_different(firstDealResult, secondDealResult);
    }

    private void should_be_different(DealResult firstDealResult, DealResult secondDealResult) {
        assertThat(firstDealResult.discardCard()).isNotEqualTo(secondDealResult.discardCard());
        assertThat(firstDealResult.drawPileCards()).isNotEqualTo(secondDealResult.drawPileCards());
        for (int i = 0; i < 4; i++) {
            assertThat(firstDealResult.playersHandCard().get(i)).isNotEqualTo(secondDealResult.playersHandCard().get(i));
        }
    }

    private void should_has_83_cards_in_draw_pile_cards() {
        assertThat(dealResult.drawPileCards()).hasSize(83);
    }

    private void should_has_discard_card() {
        assertThat(dealResult.discardCard()).isNotNull();
    }

    private void should_provide_different_hand_cards_for_four_players() {
        assertThat(dealResult.playersHandCard()).hasSize(4);
    }

    private void should_has_total_112_non_duplicate_cards_in_draw_result() {
        List<Card> cards = new ArrayList<>();
        cards.add(dealResult.discardCard());
        cards.addAll(dealResult.drawPileCards());
        dealResult.playersHandCard().stream().map(HandCard::getCards).flatMap(List::stream).forEach(cards::add);

        assertThat(new HashSet<>(cards)).hasSize(112);
    }

    private void when_deal() {
        dealResult = Dealer.deal();
    }
}