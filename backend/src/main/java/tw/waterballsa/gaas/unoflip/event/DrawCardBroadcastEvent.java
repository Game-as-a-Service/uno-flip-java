package tw.waterballsa.gaas.unoflip.event;

public record DrawCardBroadcastEvent(int eventType, String playerId, int action, String nextPlayer) {
}
