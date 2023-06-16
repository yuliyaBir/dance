package be.vdab.dance.resources;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import static org.assertj.core.api.Assertions.assertThat;
import javax.sql.DataSource;
import java.sql.SQLException;

@JdbcTest
public class DataSourceTest {
    private final DataSource dataSource;

    public DataSourceTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Test
    void getConnection() throws SQLException {
        try (var connection = dataSource.getConnection()){
            assertThat(connection.getCatalog()).isEqualTo("dance");
        }
    }
}
