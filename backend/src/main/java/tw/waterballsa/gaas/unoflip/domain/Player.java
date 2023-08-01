package tw.waterballsa.gaas.unoflip.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Player {

    private final PlayerInfo playerInfo;
    @Setter
    private HandCard handCard;

    public Player(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public int getPosition() {
        return playerInfo.position();
    }

    public String getId() {
        return playerInfo.id();
    }
}
