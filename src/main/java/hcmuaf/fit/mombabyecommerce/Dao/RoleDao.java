package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Role;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface RoleDao {
    @RegisterConstructorMapper(Role.class)
    public interface RoleDAO {
        @SqlQuery(value = """
                SELECT *
                from role
                where isActive= 1 and id != 1
                """)
        List<Role> getRoles();


        @SqlUpdate(value = """
                INSERT INTO role ( roleType, name, description, isActive)
                values (:roleType, :name, :description, :isActive)
                """)
        int addRole(@BindBean Role role);
    }
}
