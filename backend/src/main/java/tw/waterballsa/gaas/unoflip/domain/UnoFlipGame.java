package tw.waterballsa.gaas.unoflip.domain;

import lombok.Getter;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.domain.eumns.Direction;
import tw.waterballsa.gaas.unoflip.domain.eumns.GameMode;
import tw.waterballsa.gaas.unoflip.domain.eumns.GameStatus;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnoFlipGame {
    private static final int MAX_PLAYER_NUMBER = 4;
    private final List<Card> drawPileList = new ArrayList<>();
    private final List<Card> discardPileList = new ArrayList<>();

    @Getter
    private final Players players = new Players();
    @Getter
    private final int tableId;
    @Getter
    private GameStatus status;
    @Getter
    private Direction direction;
    @Getter
    private GameMode mode;
    private int actionPosition;

    public UnoFlipGame(int tableId) {
        this.tableId = tableId;
        this.status = GameStatus.WAITING;
        this.direction = Direction.RIGHT;
        this.mode = GameMode.LIGHT;
    }

    public List<Card> getDrawPile() {
        return Collections.unmodifiableList(drawPileList);
    }

    public List<Card> getDiscardPile() {
        return Collections.unmodifiableList(discardPileList);
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYER_NUMBER;
    }

    public List<PlayerInfo> getPlayerInfoList() {
        return players.toInfoList();
    }

    public String getActionPlayerId() {
        return players.getPlayerId(actionPosition);
    }

    public void join(String playerId, String playerName) {
        if (isPlayerAlreadyInGame(playerId)) {
            throw new RuntimeException("player already in game");
        }

        players.add(new PlayerInfo(playerId, playerName, getAvailablePosition()));
    }

    public void start() {
        status = GameStatus.STARTED;
        actionPosition = getInitActionPosition();

        DealResult dealResult = Dealer.deal();

        setPlayersHandCard(dealResult);
        discardPileList.add(dealResult.discardCard());
        drawPileList.addAll(dealResult.drawPileCards());
    }

    public DrawCardResult draw(String playerId) {
        if (GameStatus.STARTED != getStatus()) {
            throw new IllegalStateException("Game is not started");
        }

        if (actionPosition != players.getPlayerPosition(playerId)) {
            throw new IllegalStateException("It is not " + playerId + " turn");
        }

        Card card = drawPileList.remove(0);
        players.addPlayerHandCard(playerId, card);
        return new DrawCardResult(playerId, card, players.getPlayerId(getNextPosition()), players.getIds());
    }

    private boolean isPlayerAlreadyInGame(String playerId) {
        return players.exists(playerId);
    }

    private int getAvailablePosition() {
        if (isFull()) {
            throw new IllegalStateException("game is full");
        }

        return players.size() + 1;
    }

    private int getInitActionPosition() {
        return (int) (Math.random() * MAX_PLAYER_NUMBER) + 1;
    }

    private void setPlayersHandCard(DealResult dealResult) {
        int handCardListIdx = 0;
        for (String playerId : players.getIds()) {
            players.setHandCard(playerId, dealResult.playersHandCard().get(handCardListIdx++));
        }
    }

    private int getNextPosition() {
        return actionPosition + 1 > 4 ? 1 : ++actionPosition;
    }

    void setActionPosition(int position) {
        actionPosition = position;
    }

    Card getTopDrawCard() {
        return drawPileList.get(0);
    }
}
