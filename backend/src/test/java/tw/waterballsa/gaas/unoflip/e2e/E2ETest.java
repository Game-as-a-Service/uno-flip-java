package tw.waterballsa.gaas.unoflip.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tw.waterballsa.gaas.unoflip.enums.StatusCode;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.PlayerInfo;
import tw.waterballsa.gaas.unoflip.vo.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class E2ETest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void join_game() throws Exception {
        String playerAId = "playerA123";
        String playerAName = "PlayerA";

        Response<JoinResult> responseOfPlayerA = when_send(playerAId, playerAName);

        then_join_success(responseOfPlayerA);

        String playerBId = "playerB456";
        String playerBName = "PlayerB";

        Response<JoinResult> responseOfPlayerB = when_send(playerBId, playerBName);
        then_join_success(responseOfPlayerB);

        should_in_the_same_game(playerAId, responseOfPlayerA, responseOfPlayerB);
    }

    private void should_in_the_same_game(String playerAId, Response<JoinResult> responseOfPlayerA, Response<JoinResult> responseOfPlayerB) {
        Assertions.assertThat(responseOfPlayerA.payload().tableId()).isEqualTo(responseOfPlayerB.payload().tableId());
        Assertions.assertThat(responseOfPlayerB.payload().otherPlayerInfo().stream().map(PlayerInfo::playerId).anyMatch(playerAId::equals)).isTrue();
    }

    private void then_join_success(Response<JoinResult> responseOfPlayerA) {
        Assertions.assertThat(responseOfPlayerA)
                .hasFieldOrPropertyWithValue("code", StatusCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", "join successfully");
    }

    private Response<JoinResult> when_send(String playerId, String playerName) throws Exception {
        String response = mockMvc.perform(post("http://localhost:" + port + "/join/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new JoinRequest(playerName))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(response, new TypeReference<>() {
        });
    }

}
