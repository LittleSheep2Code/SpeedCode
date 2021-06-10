package SpeedCodeBKD.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class Executor {

    @Autowired
    public static JdbcTemplate target;

    public static void executeScript(String sql) { target.execute(sql); }
}
