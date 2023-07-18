package tw.waterballsa.gaas.unoflip.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tw.waterballsa.gaas.unoflip.domain.UnoFlipGame;

import static org.assertj.core.api.Assertions.assertThat;

class GameRepoTest {

    private GameRepo sut;

    @BeforeEach
    void setUp() {
        sut = new GameRepo();
    }

    @Test
    void table_id_not_exist() {
        Assertions.assertThat(sut.get(123)).isNotPresent();
    }

    @Test
    void table_id_exist() {
        sut.save(new UnoFlipGame(123));

        Assertions.assertThat(sut.get(123)).isPresent();
    }

    @Test
    void no_available_game() {
        assertThat(sut.getAvailable()).isNotPresent();
    }

    @Test
    void has_available_game() {
        UnoFlipGame game = new UnoFlipGame(123);

        sut.save(game);

        assertThat(sut.getAvailable()).isPresent().hasValue(game);
    }

    @Test
    void saved_game_is_null() {
        Assertions.assertThatThrownBy(() -> sut.save(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Save game failed, game is null.");
    }

    @Test
    void should_updated_when_save_the_same_game() {
        UnoFlipGame game = new UnoFlipGame(123);
        when_save(game);
        then_game_has_no_player();

        game.join("shadow123", "Shadow");
        when_save(game);
        then_game_has_one_player();
    }

    @Test
    void generate_table_id() {
        assertThat(sut.generateTableId()).isEqualTo(1);
        assertThat(sut.generateTableId()).isEqualTo(2);
        assertThat(sut.generateTableId()).isEqualTo(3);
    }

    private void then_game_has_one_player() {
        assertThat(sut.get(123)).isPresent().hasValueSatisfying(g -> assertThat(g.getPlayerInfoList()).hasSize(1));
    }

    private void then_game_has_no_player() {
        assertThat(sut.get(123)).isPresent().hasValueSatisfying(g -> assertThat(g.getPlayerInfoList()).isEmpty());
    }

    private void when_save(UnoFlipGame game) {
        sut.save(game);
    }
}