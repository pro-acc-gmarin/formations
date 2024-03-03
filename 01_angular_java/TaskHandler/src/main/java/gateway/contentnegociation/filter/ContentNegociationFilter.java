package gateway.contentnegociation.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class ContentNegociationFilter extends HttpFilter {

    @Override
    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final String contentType = request.getHeader("Accept");
        final String methodHTTP = request.getMethod();
        switch (methodHTTP) {
            case "POST":
            case "PUT":
                if(Objects.nonNull(contentType)){
                        if(!"application/json".equals(contentType)) {
                            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "MIME type not supported.");
                            return;
                        }
                }else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "MIME type not specified.");
                    return;
                }
            default:
                chain.doFilter(request, response);
        }
    }
}
