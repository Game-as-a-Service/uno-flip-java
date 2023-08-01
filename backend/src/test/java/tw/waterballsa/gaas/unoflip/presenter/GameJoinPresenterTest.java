package tw.waterballsa.gaas.unoflip.presenter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.JoinBroadcastEvent;
import tw.waterballsa.gaas.unoflip.vo.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameJoinPresenterTest {

    private GameJoinPresenter sut;

    @BeforeEach
    void setUp() {
        sut = new GameJoinPresenter();
    }

    @Test
    void response_with_no_other_player() {
        PlayerInfo myPlayerInfo = prepare_player_info("playerId2", "playerName2", 2);
        GameJoinResult gameJoinResult = prepare_game_join_result(myPlayerInfo);

        Response<JoinResult> actual = sut.response("playerId2", gameJoinResult);

        verify_response(actual, Collections.emptyList());
    }

    @Test
    void response_with_other_player() {
        PlayerInfo otherPlayerInfo = prepare_player_info("otherPlayerId", "otherPlayerName", 1);
        PlayerInfo targetPlayerInfo = prepare_player_info("targetPlayerId", "targetPlayerName", 2);
        GameJoinResult gameJoinResult = prepare_game_join_result(otherPlayerInfo, targetPlayerInfo);

        Response<JoinResult> actual = sut.response("targetPlayerId", gameJoinResult);

        verify_response(actual, Collections.singletonList(otherPlayerInfo));
    }

    @Test
    void broadcast_event() {
        PlayerInfo otherPlayerInfo = prepare_player_info("otherPlayerId", "otherPlayerName", 1);
        PlayerInfo targetPlayerInfo = prepare_player_info("targetPlayerId", "targetPlayerName", 2);
        GameJoinResult gameJoinResult = prepare_game_join_result(otherPlayerInfo, targetPlayerInfo);

        BroadcastEvent actual = sut.broadcastEvent("targetPlayerId", gameJoinResult);
        verify_broadcast(actual);
    }

    private void verify_broadcast(BroadcastEvent actual) {
        JoinBroadcastEvent targetJoinBroadcastEvent = new JoinBroadcastEvent(EventType.JOIN.getCode(), "targetPlayerId", "targetPlayerName", 2);
        BroadcastEvent expected = new BroadcastEvent(Arrays.asList("otherPlayerId", "targetPlayerId"), targetJoinBroadcastEvent);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private void verify_response(Response<JoinResult> actual, List<PlayerInfo> otherPlayerInfo1) {
        Response<JoinResult> expected = new Response<>(StatusCode.OK.getCode(), "join successfully",
                new JoinResult(123, 2, otherPlayerInfo1));

        assertThat(actual).isEqualTo(expected);
    }

    private GameJoinResult prepare_game_join_result(PlayerInfo... playerInfos) {
        return new GameJoinResult(123, Arrays.asList(playerInfos));
    }

    private PlayerInfo prepare_player_info(String playerId, String playerName, int position) {
        return new PlayerInfo(playerId, playerName, position);
    }
}