package be.vdab.dance.repositories;

import be.vdab.dance.domain.Festival;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
@JdbcTest
@Import(FestivalRepository.class)
@Sql("/festivals.sql")
class FestivalRepositoryTest extends
        AbstractTransactionalJUnit4SpringContextTests {
    private static final String FESTIVALS = "festivals";
    private final FestivalRepository festivalRepository;

    public FestivalRepositoryTest(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }
    private long idVanTestFestival1(){
        return jdbcTemplate.queryForObject(
                "select id from festivals where naam = 'test1'",
                Long.class);
    }

    @Test
    void findAllGeeftAlleFestivalsGesoorteerdOpNaam() {
        assertThat(festivalRepository.findAll())
                .hasSize(countRowsInTable(FESTIVALS))
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    void findUitverkochtGeeftDeUitverkochteFestivalsGesoorteerdOpNaam() {
        assertThat(festivalRepository.findUitverkocht())
                .hasSize(countRowsInTableWhere(FESTIVALS, "ticketsBeschikbaar = " + 0))
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }
    @Test
    void delete() {
        var id = idVanTestFestival1();
        festivalRepository.delete(id);
        assertThat(countRowsInTableWhere(FESTIVALS, "id = " + id)).isZero();

    }

    @Test
    void create() {
        var id = festivalRepository.create(new Festival(1, "festival4",56, BigDecimal.TEN));
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(FESTIVALS, "id = " + id)).isOne();
    }

    @Test
    void findAndLockById() {
        assertThat(festivalRepository.findAndLockById(idVanTestFestival1())).hasValueSatisfying(
                festival -> assertThat(festival.getNaam()).isEqualTo("test1"));
    }
    @Test
    void findAndLockByOnbestaandeIdVindtGeenMens() {
        assertThat(festivalRepository.findAndLockById(Long.MAX_VALUE)).isEmpty();
    }
    @Test
    void findAantal() {
        assertThat(festivalRepository.findAantal())
                .isEqualTo(countRowsInTable(FESTIVALS));
    }

    @Test
    void verhoogBudgetMet10() {
        festivalRepository.verhoogBudget(BigDecimal.TEN);
        var id = idVanTestFestival1();
        assertThat(countRowsInTableWhere(FESTIVALS, "reclameBudget = 110 and id = " + id)).isOne();
    }
    @Test
    void veminderBeschikbareTicketsMet2() {
        var id = idVanTestFestival1();
        festivalRepository.updateBeschikbareTickets(id, 2);
        assertThat(countRowsInTableWhere(FESTIVALS,
                "ticketsBeschikbaar = 8 and id = " + id)).isOne();
    }
}