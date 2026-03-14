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
            "VALUES (:fullName, :displayName, :email, :passwordUserName,'PENDING',:confirmationToken,:salt,:facebookId)")
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
// check lại dưới db
    @SqlQuery(value = "SELECT u.id, u.fullName, u.displayName, u.dOB, u.gender, u.email, u.phoneNumber,\n" +
            "        i.url as avatarUrl, u.status, u.confirmationToken, u.passwordUserName, u.salt, u.facebookId , u.needRefresh , \n" +
            "        r.id as role_id, r.roleType as role_roleType, r.name as role_name, r.description as role_description, r.isActive as role_isActive\n" +
            "FROM users as u\n" +
            "    left join image as i on u.avatarId = i.id\n" +
            "    left join user_role as ur on u.id = ur.userId\n" +
            "    left join role as r on ur.roleId = r.id\n" +
            "WHERE u.confirmationToken = :token")
    User getUserByConfirmationToken(@Bind("token") String token);

    @SqlUpdate("UPDATE users SET status = :status WHERE confirmationToken = :token")
    void updateUserStatusByToken(@Bind("token") String token, @Bind("status") String status);

    @SqlQuery(value = "select u.id, u.fullName, u.displayName, u.dOB, u.gender, u.email, u.phoneNumber,\n" +
            "        i.url as avatarUrl, u.status, u.confirmationToken, u.passwordUserName, u.salt, u.facebookId,  u.needRefresh ,\n" +
            "        r.id as role_id, r.roleType as role_roleType, r.name as role_name, r.description as role_description, r.isActive as role_isActive\n" +
            "from users as u\n" +
            "    left join image as i on u.avatarId = i.id\n" +
            "    left join user_role as ur on u.id = ur.userId\n" +
            "    left join role as r on ur.roleId = r.id\n" +
            "where u.id  = :id")
    User getUserById(@Bind("id") Integer id);
}
