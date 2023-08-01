package tw.waterballsa.gaas.unoflip.domain;

import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnoFlipGame {
    private static final int MAX_PLAYER_NUMBER = 4;
    private final int tableId;
    private final List<PlayerInfo> playerInfoList = new ArrayList<>();

    public UnoFlipGame(int tableId) {
        this.tableId = tableId;
    }

    public int getTableId() {
        return tableId;
    }

    public void join(String playerId, String playerName) {
        if (isPlayerAlreadyInGame(playerId)) {
            throw new RuntimeException("player already in game");
        }

        playerInfoList.add(new PlayerInfo(playerId, playerName, getAvailablePosition()));
    }

    public List<PlayerInfo> getPlayerInfoList() {
        return Collections.unmodifiableList(playerInfoList);
    }

    public boolean isFull() {
        return playerInfoList.size() >= MAX_PLAYER_NUMBER;
    }

    private boolean isPlayerAlreadyInGame(String playerId) {
        return playerInfoList.stream().anyMatch(playerInfo -> playerId.equals(playerInfo.playerId()));
    }

    private int getAvailablePosition() {
        if (isFull()) {
            throw new RuntimeException("game is full");
        }

        return playerInfoList.size() + 1;
    }

    public GameStatus getStatus() {
        return null;
    }

    public List<Card> getDrawPile() {
        return null;
    }

    public List<Card> getDiscardPile() {
        return null;
    }

    public List<Card> getPlayerHandCard(String playerId) {
        return null;
    }

    public String getActionPlayerId() {
        return null;
    }

    public Direction getDirection() {
        return null;
    }
}
