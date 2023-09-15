package tw.waterballsa.gaas.unoflip.vo;

import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;

import java.util.List;

public record GameJoinResult(int tableId, List<PlayerInfo> playerInfoList) {
}
