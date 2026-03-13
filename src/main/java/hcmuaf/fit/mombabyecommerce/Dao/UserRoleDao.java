package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.UserRole;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterConstructorMapper(UserRole.class)
public interface UserRoleDao {
    @SqlUpdate(value = """
            INSERT INTO user_role(userId, roleId)
            VALUES (:userId, :roleId)
            """)
    Integer addUserRole(@Bind("userId") Integer userId, @Bind("roleId") Integer roleId);


    @SqlUpdate(value = """
            UPDATE user_role
            set roleId = :roleId
            where userId= :userId;
            """)
    Boolean updateUserRole(@Bind("userId") Integer userId, @Bind("roleId") Integer roleId);
}
