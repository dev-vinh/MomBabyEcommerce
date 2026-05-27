package hcmuaf.fit.mombabyecommerce.controller.admin.role;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.PermissionService;
import hcmuaf.fit.mombabyecommerce.service.RoleService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageRoleController", urlPatterns = {"/admin/manage-role"})
public class ManageRoleController extends HttpServlet {
    RoleService roleService = new RoleService(DBConnection.getJdbi());
    PermissionService permissionService = new PermissionService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Role> roles = roleService.getAllRoles();
        List<Permission> permissions = permissionService.getAllPermissions();
        System.out.println("==============");
        System.out.println(permissions);
        System.out.println(permissions.get(0).getClass());
        System.out.println("==============");
        List<User> users = userService.getAllUsers();

        request.setAttribute("roles", roles);
        request.setAttribute("adminpermissions", permissions);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin/manageRole.jsp")
                .forward(request, response);
    }
}
