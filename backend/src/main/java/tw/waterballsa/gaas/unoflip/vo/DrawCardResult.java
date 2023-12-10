package tw.waterballsa.gaas.unoflip.vo;

import tw.waterballsa.gaas.unoflip.domain.eumns.Card;

import java.util.List;

public record DrawCardResult(String actionPlayerId, Card card, String nextActionPlayerId, List<String> playerIds) {

}
