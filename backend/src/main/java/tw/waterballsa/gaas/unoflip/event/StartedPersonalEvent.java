package tw.waterballsa.gaas.unoflip.event;

import java.util.List;

public record StartedPersonalEvent(int eventType, List<Integer> handCards) {

}
