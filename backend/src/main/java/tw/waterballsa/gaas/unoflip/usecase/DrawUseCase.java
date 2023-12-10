package tw.waterballsa.gaas.unoflip.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.presenter.DrawCardPresenter;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;
import tw.waterballsa.gaas.unoflip.service.SseService;
import tw.waterballsa.gaas.unoflip.vo.DrawCardResult;

@Service
@RequiredArgsConstructor
public class DrawUseCase {
    private final GameRepo gameRepo;
    private final DrawCardPresenter drawCardPresenter;
    private final SseService sseService;

    public Response<DrawResult> draw(int tableId, String playerId) {
        UnoFlipGame game = gameRepo.get(tableId).orElseThrow(() -> new IllegalStateException("game %d is not exists".formatted(tableId)));

        DrawCardResult drawCardResult = game.draw(playerId);
        gameRepo.save(game);

        sseService.sendMessage(drawCardPresenter.broadcastEvent(drawCardResult));

        return drawCardPresenter.response(drawCardResult);
    }
}
