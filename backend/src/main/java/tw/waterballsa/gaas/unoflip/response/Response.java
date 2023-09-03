package tw.waterballsa.gaas.unoflip.response;

public record Response<T>(int code, String message, T payload) {
}
