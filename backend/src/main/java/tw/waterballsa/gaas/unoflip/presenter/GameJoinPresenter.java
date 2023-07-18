package tw.waterballsa.gaas.unoflip.presenter;

import org.springframework.stereotype.Service;
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
        JoinEvent joinEvent = new JoinEvent(targetPlayerId, playerInfo.playerName(), playerInfo.position());

        return new BroadcastEvent(getPlayerIds(playerInfoList), joinEvent);
    }

    private List<PlayerInfo> getOtherPlayerInfoList(String targetPlayerId, List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream()
                .filter(playerInfo -> !targetPlayerId.equals(playerInfo.playerId()))
                .collect(Collectors.toList());
    }

    private PlayerInfo getTargetPlayerInfo(List<PlayerInfo> gameJoinResult, String targetPlayerId) {
        return gameJoinResult.stream()
                .filter(p -> targetPlayerId.equals(p.playerId())).findFirst()
                .orElseThrow(() -> new RuntimeException("should not happened, player %s not in game join result".formatted(targetPlayerId)));
    }

    private List<String> getPlayerIds(List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream().map(PlayerInfo::playerId).collect(Collectors.toList());
    }
}
