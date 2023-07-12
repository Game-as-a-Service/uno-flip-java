package tw.waterballsa.gaas.unoflip.presenter;

public enum StatusCode {
    OK(0);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
