package tw.waterballsa.gaas.unoflip.presenter;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.vo.GameJoinResult;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;
import tw.waterballsa.gaas.unoflip.vo.Response;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameJoinPresenter {
    public Response<JoinResult> present(String playerId, GameJoinResult gameJoinResult) {
        JoinResult joinResult = new JoinResult(gameJoinResult.tableId(),
                getPlayerPosition(playerId, gameJoinResult.playerInfoList()),
                getOtherPlayerInfoList(playerId, gameJoinResult.playerInfoList()));
        return new Response<>(StatusCode.OK.getCode(), "join successfully", joinResult);
    }

    private int getPlayerPosition(String playerId, List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream().filter(playerInfo -> playerId.equals(playerInfo.playerId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("should not happened, player " + playerId + ", not in game join result"))
                .position();
    }

    private List<PlayerInfo> getOtherPlayerInfoList(String playerId, List<PlayerInfo> playerInfoList) {
        return playerInfoList.stream()
                .filter(playerInfo -> !playerId.equals(playerInfo.playerId()))
                .collect(Collectors.toList());
    }
}
