package be.vdab.dance.repositories;

import be.vdab.dance.domain.Boeking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;

@Repository
public class BoekingRepository {
    private final JdbcTemplate jdbcTemplate;
//    private final RowMapper<Boeking> boekingMapper =
//            (result, rowNum) ->
//                    new Boeking(
//                            result.getString("naam"),
//                            result.getInt("aantalTickets"),
//                            result.getLong("festivalId")
//                    );

    public BoekingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long create(Boeking boeking) {
        var sql = """
                insert into boekingen(naam, aantalTickets, festivalId)
                values (?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    var statement = connection.prepareStatement(sql,
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    statement.setString(1, boeking.getNaam());
                    statement.setInt(2, boeking.getAantalTickets());
                    statement.setLong(3,boeking.getFestivalId());
                    return statement;
                }
                ,
                keyHolder);
        return keyHolder.getKey().longValue();
    }
}
