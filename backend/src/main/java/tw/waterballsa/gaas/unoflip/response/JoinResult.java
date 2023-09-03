package tw.waterballsa.gaas.unoflip.response;

import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;

import java.util.List;

public record JoinResult(Integer tableId, Integer position, List<PlayerInfo> otherPlayerInfo) {
}
