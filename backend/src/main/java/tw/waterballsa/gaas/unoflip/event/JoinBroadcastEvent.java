package tw.waterballsa.gaas.unoflip.event;

public record JoinBroadcastEvent(int eventType, String playerId, String playerName, int position) {
}
