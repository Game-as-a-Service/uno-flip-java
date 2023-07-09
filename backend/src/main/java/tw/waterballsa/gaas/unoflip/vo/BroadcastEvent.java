package tw.waterballsa.gaas.unoflip.vo;

import java.util.List;

public record BroadcastEvent(List<String> playerIds, Object eventBody) {
}
