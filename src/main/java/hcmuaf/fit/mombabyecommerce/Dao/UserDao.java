package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import java.time.LocalDate;
import java.util.List;

@RegisterBeanMapper(User.class)

public interface UserDao {

    @SqlQuery("SELECT * FROM users")
    List<User> getAllUsers();

    @SqlQuery("""
        SELECT u.id, u.fullName, u.displayName, u.dOB, u.gender,
               u.email, u.phoneNumber,
               i.url AS avatar_url
        FROM users u
        LEFT JOIN image i ON u.avatarId = i.id
        WHERE u.id = :id
    """)
    User getUserById(@Bind("id") Integer id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE google_id = :googleId")
    User getUserByGoogleId(@Bind("googleId") String googleId);

    @SqlUpdate("""
    INSERT INTO users (fullName, displayName, email, passwordUserName, salt, provider, confirmationToken, status)
    VALUES (:fullName, :displayName, :email, :passwordUserName, :salt, 'local', :confirmationToken, 'PENDING')
""")
    @GetGeneratedKeys("id")
    int createUser(@Bind("fullName") String fullName,
                   @Bind("displayName") String displayName,
                   @Bind("email") String email,
                   @Bind("passwordUserName") String passwordUserName,
                   @Bind("salt") String salt,
                   @Bind("confirmationToken") String confirmationToken);

    @SqlUpdate("""
        INSERT INTO users (fullName, displayName, email, google_id, provider, status, passwordUserName)
        VALUES (:fullName, :displayName, :email, :googleId, 'google', 'ACTIVE', :dummyPass)
    """)
    @GetGeneratedKeys("id")
    int createUserGoogle(@Bind("fullName") String fullName,
                         @Bind("displayName") String displayName,
                         @Bind("email") String email,
                         @Bind("googleId") String googleId,
                         @Bind("dummyPass") String dummyPass);

    @SqlUpdate("""
        INSERT INTO user_role (userId, roleId)
        VALUES (:userId, (SELECT id FROM roles WHERE roleType = :roleType))
    """)
    int assignRole(@Bind("userId") int userId,
                   @Bind("roleType") String roleType);

    @SqlUpdate("""
        INSERT INTO user_role (userId, roleId)
        VALUES (:userId, :roleId)
    """)
    int addRoleToUser(@Bind("userId") int userId,
                      @Bind("roleId") int roleId);

    @SqlQuery("""
    SELECT r.roleType
    FROM roles r
    JOIN user_role ur ON r.id = ur.roleId
    WHERE ur.userId = :userId
    ORDER BY 
        CASE 
            WHEN r.roleType = 'ADMIN' THEN 1
            WHEN r.roleType = 'STAFF' THEN 2
            ELSE 3
        END
    LIMIT 1
""")
    String getHighestRole(@Bind("userId") Integer userId);

    @SqlUpdate("""
        UPDATE users
        SET fullName = :fullName,
            email = :email,
            passwordUserName = :passwordUserName
        WHERE id = :id
    """)
    int updateUserBasic(@Bind("id") Integer id,
                        @Bind("fullName") String fullName,
                        @Bind("email") String email,
                        @Bind("passwordUserName") String passwordUserName);

    @SqlUpdate("""
        UPDATE users
        SET fullName = :fullName,
            displayName = :displayName,
            gender = :gender,
            phoneNumber = :phoneNumber
        WHERE id = :userId
    """)
    int updateUser(@Bind("userId") Integer userId,
                          @Bind("fullName") String fullName,
                          @Bind("displayName") String displayName,
                          @Bind("gender") String gender,
                          @Bind("phoneNumber") String phoneNumber);

    @SqlUpdate("""
        UPDATE users
        SET avatarId = :avatarId
        WHERE id = :userId
    """)
    int updateAvatar(@Bind("userId") Integer userId,
                     @Bind("avatarId") Integer avatarId);

    @SqlUpdate("""
        UPDATE users
        SET passwordUserName = :passwordUserName,
            salt = :salt
        WHERE id = :id
    """)
    int updatePassword(@Bind("id") Integer id,
                       @Bind("passwordUserName") String passwordUserName,
                       @Bind("salt") String salt);

    @SqlUpdate("UPDATE users SET status = :status WHERE id = :id")
    int updateUserStatus(@Bind("id") Integer id,
                         @Bind("status") String status);

    @SqlUpdate("UPDATE users SET status = :status WHERE email = :email")
    int updateStatusByEmail(@Bind("email") String email,
                            @Bind("status") String status);

    @SqlUpdate("UPDATE users SET status = :status WHERE confirmationToken = :token")
    int updateUserStatusByToken(@Bind("token") String token,
                                @Bind("status") String status);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User getPasswordByUserId(@Bind("id") Integer userId);

    @SqlQuery("SELECT url FROM image WHERE id = :avatarId")
    String getAvatarUrlById(@Bind("avatarId") Integer avatarId);

    @SqlUpdate("""
        UPDATE users
        SET google_id = :googleId,
            provider = 'google'
        WHERE email = :email
    """)
    int linkGoogleAccount(@Bind("email") String email,
                          @Bind("googleId") String googleId);

    @SqlUpdate("""
        UPDATE users
        SET needRefresh = :needRefresh
        WHERE id = :userId
    """)
    int updateNeedRefresh(@Bind("userId") Integer userId,
                          @Bind("needRefresh") Boolean needRefresh);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    int deleteUser(@Bind("id") Integer id);

    @SqlQuery("""
    SELECT
        u.id, u.fullName, u.displayName, u.dOB, u.gender,
        u.email, u.phoneNumber,
        u.status, u.confirmationToken,
        u.passwordUserName, u.salt,
        u.google_id, u.provider,
        i.url AS avatar_url
    FROM users u
    LEFT JOIN image i ON u.avatarId = i.id
    WHERE u.confirmationToken = :token
""")
    User getUserByConfirmationToken(@Bind("token") String token);

    @SqlQuery("""
    SELECT r.id, r.roleType
    FROM roles r
    JOIN user_role ur ON r.id = ur.roleId
    WHERE ur.userId = :userId
""")
    @RegisterBeanMapper(Role.class)
    List<Role> getUserRoleObjects(@Bind("userId") Integer userId);

    @SqlQuery("""
    SELECT u.id, u.fullName, u.displayName, u.dOB, u.gender, u.email, u.phoneNumber,
           i.url AS avatar_url,
           u.status, u.confirmationToken, u.passwordUserName, u.salt, u.google_id,
           r.id as role_id, r.roleType as role_roleType, r.name as role_name, 
           r.description as role_description, r.isActive as role_isActive
    FROM users u
    LEFT JOIN image i ON u.avatarId = i.id
    LEFT JOIN user_role ur ON u.id = ur.userId
    LEFT JOIN roles r ON ur.roleId = r.id
    where r.roleType = "USER"
    """)
    List<User> getCustomers();
}
