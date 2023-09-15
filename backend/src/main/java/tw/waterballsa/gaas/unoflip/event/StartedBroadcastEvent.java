package tw.waterballsa.gaas.unoflip.event;

import java.util.List;

public record StartedBroadcastEvent(int eventType, String initPlayerId, int direction, int initDiscardCard, List<Integer> drawPile) {

}
