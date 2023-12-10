package tw.waterballsa.gaas.unoflip.event;

import lombok.Getter;

@Getter
public enum ActionType {
    DRAW(1);

    private final int code;

    ActionType(int code) {
        this.code = code;
    }

}
