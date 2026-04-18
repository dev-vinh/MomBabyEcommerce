package hcmuaf.fit.mombabyecommerce.util;

import hcmuaf.fit.mombabyecommerce.contant.EPermission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class ValidationPermissionUtil {
    public static boolean validatePermission(HttpServletRequest request, List<EPermission> requiredPermission) {
        HttpSession session = request.getSession();

        List<String> permissionStrings = (List<String>) session.getAttribute("permissions");
        List<EPermission> permissions = permissionStrings.stream()
                .map(EPermission::valueOf)
                .toList();
        boolean result = true;

        if (permissions.isEmpty()) {
            result = false;
            return result;
        }

        for (EPermission p : requiredPermission) {
            if (!permissions.contains(p)) {
                result = false;
                return result;
            }
        }
        return result;
    }
}
