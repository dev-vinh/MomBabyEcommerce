package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.contant.ERole;
import org.jdbi.v3.core.mapper.ColumnMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ERoleMapper implements ColumnMapper<ERole> {
    @Override
    public ERole map(ResultSet rs, int column, org.jdbi.v3.core.statement.StatementContext ctx)
            throws SQLException {
        return ERole.valueOf(rs.getString(column));
    }
}