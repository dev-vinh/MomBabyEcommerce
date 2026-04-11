package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterConstructorMapper(User.class)

public interface UserDao {

    @SqlQuery("SELECT * FROM users")
    List<User> getAllUsers();

    @SqlQuery(value = "select u.id, u.fullName, u.displayName, u.dOB, u.gender, u.email, u.phoneNumber,\n" +
            "        i.url as avatar_url " +
            "from users as u\n" +
            "     left join image as i on u.avatarId = i.id\n" +
            "where u.id  = :id")
    User getUserById(@Bind("id") Integer id);

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

    @SqlUpdate("UPDATE users SET fullName = :fullName, email = :email, passwordUserName = :passwordUserName WHERE id = :id")
    void updateUser(@Bind("id") Integer id,
                    @Bind("fullName") String fullName,
                    @Bind("email") String email,
                    @Bind("passwordUserName") String passwordUserName);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void deleteUser(@Bind("id") Integer id);

    @SqlUpdate("UPDATE users SET passwordUserName = :passwordUserName, salt = :salt WHERE id = :id")
    int updatePassword(@Bind("id") Integer id, @Bind("passwordUserName") String passwordUserName,
                       @Bind("salt") String salt);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User getPasswordByUserId(@Bind("id") Integer userId);

    @SqlQuery("SELECT url FROM image WHERE id = :avatarId")
    String getAvatarUrlById(@Bind("avatarId") Integer avatarId);

    @SqlUpdate(value = "UPDATE users\n" +
            "SET avatarId = :avatarId " +
            "where id = :userId")
    Boolean updateAvatar(@Bind("userId") Integer userId, @Bind("avatarId") Integer avatarId);

    @SqlUpdate(value = "UPDATE users\n" +
            "SET\n" +
            "    fullName = :fullName ,\n" +
            "    displayName = :displayName,\n" +
            "    dOB = :dOB, " +
            "    gender = :gender,\n" +
            "    phoneNumber = :phoneNumber " +
            "where id = :userId")
    Boolean updateUser(
            @Bind("userId") Integer userId,
            @Bind("fullName") String fullName,
            @Bind("displayName") String displayName,
            @Bind("dOB") LocalDate dOB,
            @Bind("gender") String gender,
            @Bind("phoneNumber") String phoneNumber);

    @SqlUpdate("UPDATE users SET status = :status WHERE email = :email")
    int updateStatusByEmail(@Bind("email") String email, @Bind("status") String status);

}
