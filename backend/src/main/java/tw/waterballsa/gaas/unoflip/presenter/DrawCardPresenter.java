package tw.waterballsa.gaas.unoflip.presenter;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.event.ActionType;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.DrawCardBroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.EventType;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

@Service
public class DrawCardPresenter {
    public Response<DrawResult> response(DrawCardResult drawCardResult) {
        return new Response<>(StatusCode.OK.getCode(), "draw successfully", new DrawResult(drawCardResult.card().getId()));
    }

    public BroadcastEvent broadcastEvent(DrawCardResult drawCardResult) {
        return new BroadcastEvent(drawCardResult.playerIds(),
                new DrawCardBroadcastEvent(EventType.DRAW.getCode(),
                        drawCardResult.actionPlayerId(),
                        ActionType.DRAW.getCode(),
                        drawCardResult.nextActionPlayerId()));
    }
}
