package tw.waterballsa.gaas.unoflip.vo;

public record Response<T>(int code, String message, T payload) {
}
