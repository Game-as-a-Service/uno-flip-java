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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import tw.waterballsa.gaas.unoflip.domain.Card;
import tw.waterballsa.gaas.unoflip.domain.GameStatus;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;
import tw.waterballsa.gaas.unoflip.repository.GameRepo;
import tw.waterballsa.gaas.unoflip.scheduler.GameStartedBroadcastMessage;
import tw.waterballsa.gaas.unoflip.scheduler.GameStartedPersonalMessage;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameStartE2ETest {
    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepo gameRepo;
    private ExecutorService executor;
    private WebTestClient client;
    private Map<String, List<String>> sseMessagesOfPlayer = new HashMap<>();
    private Map<String, CountDownLatch> sseMessageCountDownLatchOfPlayer = new HashMap<>();
    private UnoFlipGame game;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(1);
        client = WebTestClient.bindToServer().responseTimeout(Duration.ofMinutes(5)).baseUrl("http://localhost:" + port).build();
    }

    @Test
    void game_start() throws Exception {
        given_player_join_game("player111", "playerA");
        given_player_join_game("player222", "playerB");
        given_player_join_game("player333", "playerC");
        given_player_join_game("player444", "playerD");

        when_start_game();

        then_game_exists();
        then_game_is_started();
        then_draw_pile_has_83_cards();
        then_discard_pile_has_1_card();

        should_send_personal_message_for("player111");
        should_send_personal_message_for("player222");
        should_send_personal_message_for("player333");
        should_send_personal_message_for("player444");

        should_send_same_broadcast_message_for("player111", "player222", "player333", "player444");
    }

    private void given_player_join_game(String playerId, String playerA) throws Exception {
        register_sse_client_for_(playerId);
        send_join_game(playerId, playerA);
    }

    private void register_sse_client_for_(String playerId) {
        sseMessagesOfPlayer.put(playerId, new ArrayList<>());
        sseMessageCountDownLatchOfPlayer.put(playerId, new CountDownLatch(2));

        executor.submit(() -> client.get().uri("/sse/%s".formatted(playerId)).accept(MediaType.TEXT_EVENT_STREAM).exchange()
                .expectStatus().isOk().returnResult(String.class).getResponseBody().toStream()
                .forEach(response -> {
                    sseMessagesOfPlayer.get(playerId).add(response);
                    sseMessageCountDownLatchOfPlayer.get(playerId).countDown();
                }));
    }

    private void should_send_same_broadcast_message_for(String... playerIds) throws JsonProcessingException {
        for (String playerId : playerIds) {
            String broadcastMessage = sseMessagesOfPlayer.get(playerId).get(1);
            GameStartedBroadcastMessage gameStartedBroadcastMessage = mapper.readValue(broadcastMessage, new TypeReference<>() {
            });

            Assertions.assertThat(gameStartedBroadcastMessage.getInitPlayerId()).isEqualTo(game.getActionPlayerId());
            Assertions.assertThat(gameStartedBroadcastMessage.getInitDiscardCard()).isEqualTo(game.getDiscardPile().get(0).getId());
            Assertions.assertThat(gameStartedBroadcastMessage.getDirection()).isEqualTo(game.getDirection().getCode());
            Assertions.assertThat(gameStartedBroadcastMessage.getDrawPile()).isEqualTo(game.getDrawPile());
        }
    }

    private void should_send_personal_message_for(String playerId) throws JsonProcessingException {
        List<Card> excepted = game.getPlayerHandCard(playerId);

        String personalMessage = sseMessagesOfPlayer.get(playerId).get(0);
        List<Card> actual = mapper.readValue(personalMessage, new TypeReference<GameStartedPersonalMessage>() {
        }).getHandCards();

        Assertions.assertThat(actual).isEqualTo(excepted);
    }

    private void then_discard_pile_has_1_card() {
        Assertions.assertThat(game.getDiscardPile()).hasSize(1);
    }

    private void then_draw_pile_has_83_cards() {
        Assertions.assertThat(game.getDrawPile()).hasSize(83);
        ;
    }

    private void then_game_is_started() {
        Assertions.assertThat(game.getStatus()).isEqualTo(GameStatus.STARTED);
    }

    private void then_game_exists() {
        Optional<UnoFlipGame> game = gameRepo.get(1);
        Assertions.assertThat(game).isPresent();
        this.game = game.get();
    }

    private void when_start_game() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

    private void send_join_game(String playerId, String playerName) throws Exception {
        mockMvc.perform(post("http://localhost:" + port + "/join/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new JoinRequest(playerName))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}