package be.vdab.dance.repositories;

import be.vdab.dance.domain.Festival;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FestivalRepository {
    private final JdbcTemplate template;
    private final RowMapper<Festival> festivalMapper =
            (result, rowNum) ->
                    new Festival(
                            result.getLong("id"),
                            result.getString("naam"),
                            result.getInt("ticketsBeschikbaar"),
                            result.getBigDecimal("reclameBudget")
                    );
    public FestivalRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    public List<Festival> findAll(){
        var sql = """
                select id, naam, ticketsBeschikbaar, reclameBudget
                from festivals
                order by naam
                """;
        return template.query(sql, festivalMapper);
    }
    public List<Festival> findUitverkocht(){
        var sql = """
                select id, naam, ticketsBeschikbaar, reclameBudget
                from festivals
                where ticketsBeschikbaar = 0
                order by naam
                """;
        return template.query(sql, festivalMapper);
    }

    public void delete(long id) {
        var sql = """
                delete from festivals
                where id = ?
                 """;
        template.update(sql, id);
    }

    public long create(Festival festival) {
        var sql = """
                insert into festivals(naam, ticketsBeschikbaar, reclameBudget)
                values(?, ?, ?)
                 """;
        var keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            var statement = connection.prepareStatement(
                    sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, festival.getNaam());
            statement.setInt(2, festival.getTicketsBeschikbaar());
            statement.setBigDecimal(3, festival.getReclameBudget());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Festival> findAndLockById(long id) {
        try {
            var sql = """
                    select id, naam, ticketsBeschikbaar, reclameBudget
                    from festivals
                    where id = ?
                    for update
                    """;
            return Optional.of(template.queryForObject(sql, festivalMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
    public long findAantal(){
        var sql = """
                    select count(*)
                    from festivals                   
                    """;
        return template.queryForObject(sql, Long.class);
    }
    public void verhoogBudget(BigDecimal bedrag){
        var sql = """
                    update festivals
                    set reclameBudget = reclameBudget + ?                  
                    """;
        template.update(sql, bedrag);
    }
    public void updateBeschikbareTickets(long id, int aantalTickets){
        var sql = """
                    update festivals
                    set ticketsBeschikbaar = ticketsBeschikbaar - ? 
                    where id = ?                 
                    """;
        template.update(sql, aantalTickets, id);
    }
}
