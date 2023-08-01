package tw.waterballsa.gaas.unoflip.event;

import java.util.List;

public record BroadcastEvent(List<String> playerIds, Object eventBody) {
}
