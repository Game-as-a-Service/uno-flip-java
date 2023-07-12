package tw.waterballsa.gaas.unoflip.vo;

import java.util.List;

public record JoinResult(Integer tableId, Integer position, List<PlayerInfo> otherPlayerInfo) {
}
