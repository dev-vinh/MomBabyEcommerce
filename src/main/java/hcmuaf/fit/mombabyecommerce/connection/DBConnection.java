package hcmuaf.fit.mombabyecommerce.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DBConnection {
    public static final Jdbi jdbi;
    static {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = ConfigLoader.get("db.url");
            String username = ConfigLoader.get("db.username");
            String password = ConfigLoader.get("db.password");


            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);

            HikariDataSource dataSource = new HikariDataSource(config);



//            jdbi = Jdbi.create(url, username, password);
            jdbi = Jdbi.create(dataSource);
            jdbi.installPlugin(new SqlObjectPlugin());



        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Jdbi getJdbi() {

        return jdbi;
    }
}
