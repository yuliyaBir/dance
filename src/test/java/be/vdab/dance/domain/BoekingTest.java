package be.vdab.dance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;


class BoekingTest {

    @Test
    void eenBoekingDieLukt() {
        new Boeking("mie", 2, 1);
    }
    @Test
    void deNaamIsVerplicht() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Boeking("",2,1));
    }
    @Test
    void nulTicketsBoekenKanNiet() {
        assertThatIllegalArgumentException().isThrownBy(()->new Boeking("mie",0,1));
    }
    @Test
    void eenNegatiefAantalTicketsBoekenKanNiet() {
        assertThatIllegalArgumentException().isThrownBy(()->new Boeking("mie",-1,1));
    }
    @Test
    void deFestivalIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(()->new Boeking("mie",2,0));
    }
}