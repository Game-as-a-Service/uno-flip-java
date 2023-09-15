package tw.waterballsa.gaas.unoflip.event;

import lombok.Getter;

@Getter
public enum EventType {
    JOIN(1),
    STARTED(2),
    HAND_CARD(3),
    DRAW(4),
    PLAY(5),
    COLOR(6),
    UNO(7),
    CATCH(8),
    ENDED(9);

    private final int code;

    EventType(int code) {
        this.code = code;
    }
}
