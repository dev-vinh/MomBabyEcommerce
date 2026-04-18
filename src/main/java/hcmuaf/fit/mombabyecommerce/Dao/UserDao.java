package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
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

    @SqlQuery("SELECT u.*, " +
          "r.id AS r_id, " +
          "r.roleType AS r_roleType, " +
          "r.name AS r_name, " +
          "r.description AS r_description " +
          "FROM users u " +
          "LEFT JOIN user_role ur ON u.id = ur.userId " +
          "LEFT JOIN roles r ON ur.roleId = r.id " +
          "WHERE u.email = :email")
    @RegisterBeanMapper(value = User.class)
    @RegisterBeanMapper(value = Role.class, prefix = "r")
    User getUserByEmail(@Bind("email") String email);

    @SqlUpdate("INSERT INTO users (fullName, displayName, email, passwordUserName, salt,provider) " +
            "VALUES (:fullName, :displayName, :email, :passwordUserName, :salt,'local')")
    @GetGeneratedKeys("id")
    int createUser(@Bind("fullName") String fullName,
                      @Bind("displayName") String displayName,
                      @Bind("email") String email,
                      @Bind("passwordUserName") String passwordUserName,
                      @Bind("salt") String salt);

    @SqlUpdate("INSERT INTO user_role (userId, roleId) VALUES (:userId, (SELECT id FROM roles WHERE roleType = :roleType))")
    void assignRole(@Bind("userId") int userId, @Bind("roleType") String roleType);

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
//tim user theo gg id
    @SqlQuery("SELECT * FROM users WHERE google_id = :googleId")
    User getUserByGoogleId(@Bind("googleId") String googleId);
// tao user với gg
    @SqlUpdate("INSERT INTO users (fullName, displayName, email, google_id, provider, status,passwordUserName) " +
            "VALUES (:fullName, :displayName, :email, :googleId, 'google', 'ACTIVE',:dummyPass)")
    @GetGeneratedKeys("id")
    int createUserGoogle(@Bind("fullName") String fullName,
                         @Bind("displayName") String displayName,
                         @Bind("email") String email,
                         @Bind("googleId") String googleId,
                         @Bind("dummyPass") String dummyPass);
    @SqlUpdate("INSERT INTO user_role (userId, roleId) VALUES (:userId, :roleId)")
    void addRoleToUser(@Bind("userId") int userId, @Bind("roleId") int roleId);
// lien ket tk local len gg
    @SqlUpdate("UPDATE users SET google_id = :googleId, provider = 'google' WHERE email = :email")
    void linkGoogleAccount(@Bind("email") String email, @Bind("googleId") String googleId);
//lay ds role của 1 user
    @SqlQuery("SELECT r.roleType FROM roles r " +
            "JOIN user_role ur ON r.id = ur.roleId " +
            "WHERE ur.userId = :userId")
    List<String> getUserRoles(@Bind("userId") Integer userId);

    @SqlUpdate("UPDATE users SET status = :status WHERE id = :id")
    void updateUserStatus(@Bind("id") Integer id, @Bind("status") String status);

    @RegisterConstructorMapper(value = User.class)
    @RegisterConstructorMapper(value = Role.class, prefix = "role")
    @SqlQuery("SELECT " +
            "u.id, u.fullName, u.displayName, u.dOB, u.gender, u.email, u.phoneNumber, " +
            "u.status, u.confirmationToken, u.passwordUserName, u.salt, u.google_id, u.provider, u.needRefresh, " +
            "i.url AS avatar_url, " +
            "r.id AS role_id, r.roleType AS role_roleType, r.name AS role_name, " +
            "r.description AS role_description, r.isActive AS role_isActive " +
            "FROM users u " +
            "LEFT JOIN image i ON u.avatarId = i.id " +
            "LEFT JOIN user_role ur ON u.id = ur.userId " +
            "LEFT JOIN roles r ON ur.roleId = r.id " +
            "WHERE u.confirmationToken = :token")
    User getUserByConfirmationToken(@Bind("token") String token);

    @SqlUpdate("UPDATE users SET status = :status WHERE confirmationToken = :token")
    void updateUserStatusByToken(@Bind("token") String token, @Bind("status") String status);

    @SqlUpdate(value = """
            UPDATE users
            set needRefresh = :needRefresh
            where id = :userId
            """)
    Boolean updateNeedRefresh(@Bind("userId") Integer userId, @Bind("needRefresh") Boolean needRefresh);

}
