package tw.waterballsa.gaas.unoflip.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.event.ActionType;
import tw.waterballsa.gaas.unoflip.event.DrawCardBroadcastEvent;
import tw.waterballsa.gaas.unoflip.event.EventType;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.response.DrawResult;
import tw.waterballsa.gaas.unoflip.response.Response;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
public class GameActionE2ETest {
    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private GameRepo gameRepo;

    private ExecutorService executor;
    private WebTestClient client;
    private String drawCardBroadcastEvent;
    private CountDownLatch countDownLatch;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(1);
        client = WebTestClient.bindToServer().responseTimeout(Duration.ofMinutes(5)).baseUrl("http://localhost:" + port).build();
    }

    @Test
    void draw_card() throws Exception {
        register_sse_client_for_shadow();

        given_game_table_id_is_1234();
        given_player_join("Shadow");
        given_player_join("Max");
        given_player_join("Hannah");
        given_player_join("Archie");
        given_game_started();

        String currentActionPlayerId = get_action_player_id();

        Response<DrawResult> response = when_action_player_draw(currentActionPlayerId);

        String nextActionPlayerId = get_action_player_id();

        then_draw_success(currentActionPlayerId, response);
        then_should_receive_draw_broadcast_message(currentActionPlayerId, nextActionPlayerId);
    }

    private String get_action_player_id() {
        UnoFlipGame game = get_game_from_repo();
        return game.getActionPlayerId();
    }

    private void then_should_receive_draw_broadcast_message(String actionPlayerId, String nextActionPlayerId) throws JsonProcessingException, InterruptedException {
        countDownLatch.await();

        DrawCardBroadcastEvent expected = new DrawCardBroadcastEvent(EventType.DRAW.getCode(), actionPlayerId, ActionType.DRAW.getCode(), nextActionPlayerId);
        DrawCardBroadcastEvent actual = mapper.readValue(drawCardBroadcastEvent, new TypeReference<>() {
        });

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private void then_draw_success(String actionPlayerId, Response<DrawResult> response) {
        UnoFlipGame game = get_game_from_repo();
        List<Integer> actionPlayerHandCards = game.getPlayers().getPlayerHandCard(actionPlayerId).toCardIds();
        assertThat(actionPlayerHandCards).contains(response.payload().card());
    }

    private UnoFlipGame get_game_from_repo() {
        return gameRepo.get(1234).orElseThrow(() -> new IllegalStateException("game 1234 is not exists"));
    }

    private Response<DrawResult> when_action_player_draw(String playerId) throws Exception {
        String response = mockMvc.perform(post("http://localhost:%d/action/draw/1234/%s".formatted(port, playerId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(response, new TypeReference<>() {
        });
    }

    private void given_game_started() {
        UnoFlipGame game = get_game_from_repo();
        game.start();
        gameRepo.save(game);
    }

    private void given_player_join(String name) {
        UnoFlipGame game = get_game_from_repo();
        game.join("%sId".formatted(name), name);
        gameRepo.save(game);
    }

    private void given_game_table_id_is_1234() {
        UnoFlipGame game = new UnoFlipGame(1234);
        gameRepo.save(game);
    }

    private void register_sse_client_for_shadow() throws InterruptedException {
        countDownLatch = new CountDownLatch(1);
        executor.submit(() -> client.get().uri("/sse/ShadowId").accept(MediaType.TEXT_EVENT_STREAM).exchange()
                .expectStatus().isOk().returnResult(String.class).getResponseBody().toStream()
                .forEach(response -> {
                    drawCardBroadcastEvent = response;
                    countDownLatch.countDown();
                }));

        TimeUnit.MILLISECONDS.sleep(800);
    }

}
