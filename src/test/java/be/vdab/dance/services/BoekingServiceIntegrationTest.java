package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.exceptions.BeschikbareTicketsNietGenoeg;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Import({BoekingService.class, BoekingRepository.class, FestivalRepository.class})
@Sql("/festivals.sql")
public class BoekingServiceIntegrationTest
        extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String FESTIVALS = "festivals";
    private static final String BOEKINGEN = "boekingen";
    private final BoekingService boekingService;

    public BoekingServiceIntegrationTest(BoekingService boekingService) {
        this.boekingService = boekingService;
    }
    private long idVanTestFestival (){
        return jdbcTemplate.queryForObject(
                "select id from festivals where naam = 'test1'", Long.class);
    }

    @Test
    void create() {
        boekingService.boeking(new Boeking("jan", 3, idVanTestFestival()));
        assertThat(countRowsInTableWhere(BOEKINGEN,
                "naam = 'jan' and aantalTickets = 3 and festivalId = " + idVanTestFestival())).isOne();
        assertThat(countRowsInTableWhere(FESTIVALS,
                "ticketsBeschikbaar = 7 and id = " + idVanTestFestival())).isOne();
    }

    @Test
    void boekingMetTeVeelTicketsMislukt() {
        assertThatExceptionOfType(BeschikbareTicketsNietGenoeg.class).isThrownBy(() ->
                boekingService.boeking(new Boeking("jan", 11, idVanTestFestival())));
    }
    @Test
    void boekingMetOnbestaandeIdFestivalMislukt() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(() ->
                boekingService.boeking(new Boeking("jan", 3, Long.MAX_VALUE)));
    }

}
