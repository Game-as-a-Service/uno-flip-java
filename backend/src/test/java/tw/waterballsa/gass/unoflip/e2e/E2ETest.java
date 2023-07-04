package tw.waterballsa.gass.unoflip.e2e;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import tw.waterballsa.gaas.unoflip.UnoFlipApplication;
import tw.waterballsa.gaas.unoflip.vo.JoinRequest;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;
import tw.waterballsa.gaas.unoflip.vo.Response;

@SpringBootTest(classes = UnoFlipApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void join_game() {
        String playerId = "playerA#123";
        String playerName = "PlayerA";
        Response<JoinResult> joinResponse = restTemplate.postForObject("http://localhost:" + port + "/join/" + playerId, new JoinRequest(playerName), Response.class);
        Assertions.assertThat(joinResponse).hasFieldOrPropertyWithValue("code", 0);
    }

}
