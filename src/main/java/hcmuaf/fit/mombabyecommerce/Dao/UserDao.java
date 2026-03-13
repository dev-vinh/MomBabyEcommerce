package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterConstructorMapper(User.class)
public interface UserDao {
    @SqlQuery("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(@Bind("email") String email);

    @SqlUpdate("INSERT INTO users (fullName, displayName, email, passwordUserName, role, salt) " +
            "VALUES (:fullName, :displayName, :email, :passwordUserName, 'USER', :salt)")
    @GetGeneratedKeys("id")
    String createUser(@Bind("fullName") String fullName,
                      @Bind("displayName") String displayName,
                      @Bind("email") String email,
                      @Bind("passwordUserName") String passwordUserName,
                      @Bind("salt") String salt);
}
