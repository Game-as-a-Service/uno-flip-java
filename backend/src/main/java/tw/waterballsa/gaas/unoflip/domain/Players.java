package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.*;

public class Players {
    private final Map<String, Player> idMap = new HashMap<>();
    private final Map<Integer, Player> positionMap = new TreeMap<>();

    public boolean exists(String playerId) {
        return idMap.get(playerId) != null;
    }

    public void add(PlayerInfo playerInfo) {
        Player player = new Player(playerInfo);
        idMap.put(playerInfo.id(), player);
        positionMap.put(playerInfo.position(), player);
    }

    public List<PlayerInfo> toInfoList() {
        return idMap.values().stream().map(Player::getPlayerInfo).toList();
    }

    public HandCard getPlayerHandCard(String playerId) {
        return getPlayer(playerId).getHandCard();
    }

    public void setHandCard(String playerId, HandCard handCard) {
        getPlayer(playerId).setHandCard(handCard);
    }

    public int size() {
        return idMap.size();
    }

    public List<String> getIds() {
        return idMap.values().stream().map(Player::getId).toList();
    }

    public String getPlayerId(int position) {
        return Optional.ofNullable(positionMap.get(position))
                .map(Player::getId)
                .orElseThrow(() -> new IllegalArgumentException("position %d not exists".formatted(position)));
    }

    public int getPlayerPosition(String playerId) {
        return getPlayer(playerId).getPosition();
    }

    public void addPlayerHandCard(String playerId, Card card) {
        getPlayer(playerId).addHandCard(card);
    }

    private Player getPlayer(String playerId) {
        return Optional.ofNullable(idMap.get(playerId)).orElseThrow(() -> new IllegalArgumentException("player %s not exists".formatted(playerId)));
    }
}
