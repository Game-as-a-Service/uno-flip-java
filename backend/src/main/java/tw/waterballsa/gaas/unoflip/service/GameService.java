package tw.waterballsa.gaas.unoflip.service;

import org.springframework.stereotype.Service;
import tw.waterballsa.gaas.unoflip.vo.JoinResult;

import java.util.Collections;

@Service
public class GameService {
    public JoinResult join(String playerId, String playerName) {
        return new JoinResult(1, 1, Collections.emptyList());
    }
}
