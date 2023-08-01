package tw.waterballsa.gaas.unoflip.presenter;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(0);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

}
