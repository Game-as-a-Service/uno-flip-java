package tw.waterballsa.gaas.unoflip.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import tw.waterballsa.gaas.unoflip.presenter.StatusCode;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.response.JoinResult;
import tw.waterballsa.gaas.unoflip.domain.PlayerInfo;
import tw.waterballsa.gaas.unoflip.response.Response;

import java.time.Duration;
import java.util.ArrayList;
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
public class GameJoinE2ETest {

    private final String PLAYER_A_ID = "playerA123";

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private WebTestClient client;
    private ExecutorService executor;
    private List<String> responseList;
    private CountDownLatch countDownLatch;

    @BeforeEach
    void setUp() {
        executor = Executors.newFixedThreadPool(1);
        client = WebTestClient.bindToServer().responseTimeout(Duration.ofMinutes(5)).baseUrl("http://localhost:" + port).build();
    }

    @AfterEach
    void tearDown() {
        executor.shutdown();
    }

    @Test
    void join_game() throws Exception {
        register_sse_client_for_playerA123();

        TimeUnit.MILLISECONDS.sleep(800);

        Response<JoinResult> playerAResponse = when_send_join(PLAYER_A_ID, "PlayerA");
        then_join_success(playerAResponse);

        Response<JoinResult> playerBResponse = when_send_join("playerB456", "PlayerB");
        then_join_success(playerBResponse);

        playerA_and_playerB_should_in_the_same_game(playerAResponse, playerBResponse);
        playerA_should_received_two_join_broadcasts();
    }

    private void playerA_should_received_two_join_broadcasts() throws InterruptedException {
        countDownLatch.await();

        assertThat(responseList).containsExactly("{\"eventType\":1,\"playerId\":\"playerA123\",\"playerName\":\"PlayerA\",\"position\":1}",
                "{\"eventType\":1,\"playerId\":\"playerB456\",\"playerName\":\"PlayerB\",\"position\":2}");
    }

    private void register_sse_client_for_playerA123() {
        responseList = new ArrayList<>();
        countDownLatch = new CountDownLatch(2);

        executor.submit(() -> client.get().uri("/sse/playerA123").accept(MediaType.TEXT_EVENT_STREAM).exchange()
                .expectStatus().isOk().returnResult(String.class).getResponseBody().toStream()
                .forEach(response -> {
                    responseList.add(response);
                    countDownLatch.countDown();
                }));
    }

    private void playerA_and_playerB_should_in_the_same_game(Response<JoinResult> responseOfPlayerA, Response<JoinResult> responseOfPlayerB) {
        assertThat(responseOfPlayerA.payload().tableId()).isEqualTo(responseOfPlayerB.payload().tableId());
        assertThat(responseOfPlayerB.payload().otherPlayerInfo().stream().map(PlayerInfo::id).anyMatch(PLAYER_A_ID::equals)).isTrue();
    }

    private void then_join_success(Response<JoinResult> responseOfPlayerA) {
        assertThat(responseOfPlayerA)
                .hasFieldOrPropertyWithValue("code", StatusCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", "join successfully");
    }

    private Response<JoinResult> when_send_join(String playerId, String playerName) throws Exception {
        String response = mockMvc.perform(post("http://localhost:" + port + "/join/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new JoinRequest(playerName))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(response, new TypeReference<>() {
        });
    }

}
