package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
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

    @SqlUpdate("INSERT INTO users (fullName, displayName, email, passwordUserName,status,confirmationToken, salt,facebookId) " +
            "VALUES (:fullName, :displayName, :email, :passwordUserName,'PENDING',:confirmationToken,:salt,:facebookId))")
    @GetGeneratedKeys("id")
    Integer createUser(@Bind("fullName") String fullName,
                      @Bind("displayName") String displayName,
                      @Bind("email") String email,
                      @Bind("passwordUserName") String passwordUserName,
                       @Bind("confirmationToken") String confirmationToken,
                      @Bind("salt") String salt,
                        @Bind("facebookId") String facebookId);

    @SqlQuery("SELECT id, roleType, name, description, isActive FROM role WHERE roleType = 'USER' LIMIT 1")
    Role getDefaultUserRole();

    @SqlUpdate(value = """
            UPDATE users
            set needRefresh = :needRefresh
            where id = :userId
            """)
    Boolean updateNeedRefresh(@Bind("userId") Integer userId, @Bind("needRefresh") Boolean needRefresh);

}
