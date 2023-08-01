package tw.waterballsa.gaas.unoflip.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Players {
    private final Map<String, Player> playerMap = new HashMap<>();

    public boolean exists(String playerId) {
        return playerMap.get(playerId) != null;
    }

    public void add(PlayerInfo playerInfo) {
        playerMap.put(playerInfo.id(), new Player(playerInfo));
    }

    public List<PlayerInfo> toInfoList() {
        return playerMap.values().stream().map(Player::getPlayerInfo).toList();
    }

    public HandCard getPlayerHandCard(String playerId) {
        return Optional.ofNullable(playerMap.get(playerId))
                .map(Player::getHandCard)
                .orElseThrow(() -> new IllegalArgumentException("player %s not exists".formatted(playerId)));
    }

    public void setHandCard(String playerId, HandCard handCard) {
        Player player = Optional.ofNullable(playerMap.get(playerId)).orElseThrow(() -> new IllegalArgumentException("player %s not exists".formatted(playerId)));
        player.setHandCard(handCard);
    }

    public int size() {
        return playerMap.size();
    }

    public List<String> getIds() {
        return playerMap.values().stream().map(Player::getId).toList();
    }

    public String getPlayerId(int position) {
        return playerMap.values().stream()
                .filter(player -> position == player.getPosition())
                .findFirst()
                .map(Player::getId)
                .orElseThrow(() -> new IllegalArgumentException("position %d not exists".formatted(position)));
    }
}
