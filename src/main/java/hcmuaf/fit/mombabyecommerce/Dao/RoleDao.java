package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(Role.class)
public interface RoleDao {
    @SqlQuery("""
    SELECT u.*
    FROM users u
    JOIN user_role ur
        ON u.id = ur.userId
    WHERE ur.roleId = :roleId
    """)
    List<User> getUsersByRoleId(
            @Bind("roleId") Integer roleId
    );

    @SqlQuery("""
        SELECT * FROM roles
        WHERE id = :id
    """)
    Role getRoleById(@Bind("id") Integer id);

    @SqlQuery("""
        SELECT * FROM roles
        WHERE roleType = :roleType
    """)
    Role getRoleByType(@Bind("roleType") String roleType);

    @SqlQuery("""
        SELECT id FROM roles
        WHERE roleType = :roleType
    """)
    Integer getRoleIdByType(@Bind("roleType") String roleType);

    @SqlUpdate("""
        INSERT INTO roles (roleType, name, description, isActive)
        VALUES (:roleType, :name, :description, :isActive)
    """)
    int addRole(@BindBean Role role);

    @SqlUpdate("""
        UPDATE roles
        SET name = :name,
            description = :description,
            isActive = :isActive
        WHERE id = :id
    """)
    int updateRole(@BindBean Role role);

    @SqlUpdate("""
        UPDATE roles
        SET isActive = 0
        WHERE id = :id
    """)
    int disableRole(@Bind("id") Integer id);

    @SqlUpdate("""
        DELETE FROM roles
        WHERE id = :id
    """)
    int deleteRole(@Bind("id") Integer id);

    @SqlQuery("""
SELECT
    r.id,
    r.roleType,
    r.name,
    r.description,
    r.isActive,

    COUNT(DISTINCT ur.userId) AS memberCount,
    COUNT(DISTINCT rp.permissionId) AS permissionCount,

    GROUP_CONCAT(
        DISTINCT p.name
        ORDER BY p.id
        SEPARATOR ', '
    ) AS permissions

FROM roles r

LEFT JOIN user_role ur
    ON ur.roleId = r.id

LEFT JOIN role_permission rp
    ON rp.roleId = r.id

LEFT JOIN permissions p
    ON p.id = rp.permissionId

WHERE r.isActive = 1

GROUP BY
    r.id,
    r.roleType,
    r.name,
    r.description,
    r.isActive
""")
    List<Role> getRoles();

}
