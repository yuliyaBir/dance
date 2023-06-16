package be.vdab.dance.domain;

import be.vdab.dance.exceptions.GeenReclameBudgetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class FestivalTest {
    private Festival rockDance, chaChaCha, tomorrowland;
    private ArrayList<Festival> festivals = new ArrayList<>();
    @BeforeEach
    void beforeEach() {
        rockDance = new Festival(1, "Rock Dance", 200, BigDecimal.valueOf(100));
        chaChaCha = new Festival(1, "Cha Cha Cha", 1000, BigDecimal.TEN);
        tomorrowland = new Festival(1,"Tomorrowland", 2, BigDecimal.valueOf(20000));
        festivals.add(chaChaCha);
        festivals.add(tomorrowland);
    }
    @Test
    void festivalAnnulerenGelukt() {
        rockDance.festivalAnnuleren(festivals);
        assertThat(chaChaCha.getReclameBudget()).isEqualByComparingTo("60");
        assertThat(tomorrowland.getReclameBudget()).isEqualByComparingTo("20050");
    }

    @Test
    void festivalAnnulerenMisluktWegensNullParameter() {
        assertThatNullPointerException().isThrownBy(() ->  rockDance.festivalAnnuleren(null));
    }
    @Test
    void idVanFestivalMoetMeerDan0Zijn() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Festival(0, "n", 45, BigDecimal.TEN));
    }
    @Test
    void naamVanFestivalMoetIngevuldZijn() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Festival(1, "", 45, BigDecimal.TEN));
    }
    @Test
    void ticketsVanFestivalMagNietNegatiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Festival(1, "n", -5, BigDecimal.TEN));
    }
    @Test
    void reclameBudgetVanFestivalMoetMeerDan0Zijn() {
        assertThatExceptionOfType(GeenReclameBudgetException.class).isThrownBy(() ->  new Festival(1, "n", 5, BigDecimal.ZERO));
    }
}