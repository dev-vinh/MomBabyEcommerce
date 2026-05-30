package hcmuaf.fit.mombabyecommerce.controller.admin.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.PermissionService;
import hcmuaf.fit.mombabyecommerce.service.RoleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/role-detail")
public class RoleDetailController extends HttpServlet {
    private final RoleService roleService =
            new RoleService(DBConnection.getJdbi());
    private final PermissionService permissionService =
            new PermissionService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer roleId =
                Integer.parseInt(request.getParameter("id"));

        List<Permission> permissions =
                permissionService.getPermissionsByRoleId(roleId);

        List<User> users =
                roleService.getUsersByRoleId(roleId);

        Map<String, Object> result =
                new HashMap<>();

        result.put("id", roleId);

        result.put(
                "permissions",
                permissions.stream()
                        .map(Permission::getId)
                        .toList()
        );

        result.put("users", users);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(
                response.getWriter(),
                result
        );
    }

}
