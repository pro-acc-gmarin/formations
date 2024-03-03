package utils.filter;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String[] getUrlParts(final HttpServletRequest request) {
        return request.getRequestURI().replace(request.getContextPath() + "/", "").split("/");
    }
}
