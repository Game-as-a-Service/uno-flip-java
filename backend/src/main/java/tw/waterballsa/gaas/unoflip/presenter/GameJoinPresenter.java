package tw.waterballsa.gaas.unoflip.presenter;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.JoinBroadcastEvent;
import tw.waterballsa.gaas.unoflip.vo.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameJoinPresenter {
    public Response<JoinResult> response(String targetPlayerId, GameJoinResult gameJoinResult) {
        List<PlayerInfo> playerInfoList = gameJoinResult.playerInfoList();

        JoinResult joinResult = new JoinResult(gameJoinResult.tableId(),
                getTargetPlayerInfo(playerInfoList, targetPlayerId).position(),
                getOtherPlayerInfoList(targetPlayerId, playerInfoList));

        return new Response<>(StatusCode.OK.getCode(), "join successfully", joinResult);
    }

    public BroadcastEvent broadcastEvent(String targetPlayerId, GameJoinResult gameJoinResult) {
        List<PlayerInfo> playerInfoList = gameJoinResult.playerInfoList();

        PlayerInfo playerInfo = getTargetPlayerInfo(playerInfoList, targetPlayerId);
        JoinBroadcastEvent joinBroadcastEvent = new JoinBroadcastEvent(EventType.JOIN.getCode(), targetPlayerId, playerInfo.name(), playerInfo.position());

        return new BroadcastEvent(getPlayerIds(playerInfoList), joinBroadcastEvent);
    }

    private List<PlayerInfo> getOtherPlayerInfoList(String targetPlayerId, List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream()
                .filter(playerInfo -> !targetPlayerId.equals(playerInfo.id()))
                .collect(Collectors.toList());
    }

    private PlayerInfo getTargetPlayerInfo(List<PlayerInfo> gameJoinResult, String targetPlayerId) {
        return gameJoinResult.stream()
                .filter(p -> targetPlayerId.equals(p.id())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("should not happened, player %s not in game join result".formatted(targetPlayerId)));
    }

    private List<String> getPlayerIds(List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream().map(PlayerInfo::id).collect(Collectors.toList());
    }
}
