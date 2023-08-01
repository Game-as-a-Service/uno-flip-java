package tw.waterballsa.gaas.unoflip.domain;

public enum Direction {
    LEFT(1),

    RIGHT(2);
    private final int code;

    Direction(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
