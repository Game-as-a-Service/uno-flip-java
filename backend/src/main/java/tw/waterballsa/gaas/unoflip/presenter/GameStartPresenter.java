package tw.waterballsa.gaas.unoflip.presenter;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.Players;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.PersonalEvent;
import tw.waterballsa.gaas.unoflip.event.StartedBroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.StartedPersonalEvent;
import tw.waterballsa.gaas.unoflip.vo.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameStartPresenter {
    public BroadcastEvent broadcastEvent(GameStartResult gameStartResult) {

        StartedBroadcastEvent startedBroadcastEvent = new StartedBroadcastEvent(
                EventType.STARTED.getCode(),
                gameStartResult.currentPlayerId(),
                gameStartResult.direction().getCode(),
                gameStartResult.discardPileCards().get(0).getId(),
                gameStartResult.drawPileCards().stream().map(Card::getId).toList());

        return new BroadcastEvent(gameStartResult.players().getIds(), startedBroadcastEvent);
    }

    public List<PersonalEvent> personalEvents(GameStartResult gameStartResult) {
        List<PersonalEvent> personalEventList = new ArrayList<>();
        Players players = gameStartResult.players();
        for (String id : players.getIds()) {
            StartedPersonalEvent event = new StartedPersonalEvent(EventType.HAND_CARD.getCode(), players.getPlayerHandCard(id).toCardIds());
            personalEventList.add(new PersonalEvent(id, event));
        }
        return personalEventList;
    }
}
