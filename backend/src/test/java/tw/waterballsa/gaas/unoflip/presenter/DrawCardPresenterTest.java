package tw.waterballsa.gaas.unoflip.presenter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.eumns.Card;
import tw.waterballsa.gaas.unoflip.event.ActionType;
import tw.waterballsa.gaas.unoflip.event.BroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.DrawCardBroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.EventType;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

import java.util.Arrays;

class DrawCardPresenterTest {

    private DrawCardPresenter sut;

    @BeforeEach
    void setUp() {
        sut = new DrawCardPresenter();
    }

    @Test
    void response() {
        Response<DrawResult> expected = new Response<>(StatusCode.OK.getCode(), "draw successfully", new DrawResult(Card.CARD_5.getId()));

        Assertions.assertThat(sut.response(given_draw_card_is_5())).isEqualTo(expected);
    }

    @Test
    void broadcast_event() {
        BroadcastEvent expected = new BroadcastEvent(
                Arrays.asList("ShadowId", "MaxId", "HannahId", "ArchieId"),
                new DrawCardBroadcastEvent(EventType.DRAW.getCode(), "ShadowId", ActionType.DRAW.getCode(), "MaxId"));

        Assertions.assertThat(sut.broadcastEvent(new DrawCardResult("ShadowId", Card.CARD_3, "MaxId",
                Arrays.asList("ShadowId", "MaxId", "HannahId", "ArchieId")))).isEqualTo(expected);
    }

    private DrawCardResult given_draw_card_is_5() {
        return new DrawCardResult("ShadowId", Card.CARD_5, "MaxId", Arrays.asList("ShadowId", "MaxId", "HannahId", "ArchieId"));
    }
}