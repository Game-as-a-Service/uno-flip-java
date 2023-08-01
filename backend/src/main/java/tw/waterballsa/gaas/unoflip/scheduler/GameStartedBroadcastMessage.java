package tw.waterballsa.gaas.unoflip.scheduler;

import tw.waterballsa.gaas.unoflip.domain.Card;

import java.util.List;

public class GameStartedBroadcastMessage {
    public String getInitPlayerId() {
        return null;
    }

    public int getDirection() {
        return 0;
    }

    public List<Integer> getDiscardPile() {
        return null;
    }

    public int getInitDiscardCard() {
        return 0;
    }

    public List<Card> getDrawPile() {
        return null;
    }
}
