package be.vdab.dance.repositories;

import be.vdab.dance.domain.Boeking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(BoekingRepository.class)
@Sql("/festivals.sql")
class BoekingRepositoryTest extends
        AbstractTransactionalJUnit4SpringContextTests {
    private static final String BOEKINGEN = "boekingen";
    private final BoekingRepository boekingRepository;
    private Boeking boeking1;

    public BoekingRepositoryTest(BoekingRepository boekingRepository) {
        this.boekingRepository = boekingRepository;
    }
    private long idVanTestFestival1(){
        return jdbcTemplate.queryForObject(
                "select id from festivals where naam = 'test1'",
                Long.class);
    }
    @BeforeEach
    void beforeEach(){
        boeking1 = new Boeking(0, "Yuliya", 2, idVanTestFestival1());
    }

    @Test
    void create() {
        var id = boekingRepository.create(boeking1);
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(BOEKINGEN, "id = " + id)).isOne();
    }
}