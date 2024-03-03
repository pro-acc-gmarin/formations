package gateway.cache.filters;

import board.application.dto.OutBoardDto;
import gateway.cache.model.DateCacheManager;
import org.picocontainer.MutablePicoContainer;
import task.application.dto.OutTaskDto;
import utils.enumerations.ServletContextKey;
import utils.filter.RequestUtils;
import utils.helpers.ServletContextHelper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateCacheFilter extends HttpFilter {

    private DateCacheManager<String, Object> cacheManager;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        final ServletContext servletContext = filterConfig.getServletContext();
        final MutablePicoContainer globalContainer = (MutablePicoContainer) ServletContextHelper.getAttribute(servletContext, ServletContextKey.GLOBAL_CONTAINER);
        this.cacheManager = (DateCacheManager<String, Object>) globalContainer.getComponent(DateCacheManager.class);
    }

    @Override
    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        this.cacheManager.clean();
        final String methodHTTP = request.getMethod();
        final ServletContext servletContext = request.getServletContext();
        final String[] urlParts = RequestUtils.getUrlParts(request);
        switch (methodHTTP) {
            case "DELETE":
                chain.doFilter(request, response);
                if (urlParts.length > 1) {
                    final String id = urlParts[1];
                    this.cacheManager.remove(id);
                }
                return;
            case "POST":
            case "PUT":
                chain.doFilter(request, response);
                this.treatmentAfter(servletContext, response);
                return;
            case "GET":
                if (urlParts.length > 1) {
                    final String id = urlParts[1];
                    final String ifModifiedSinceString = request.getHeader("If-Modified-Since");
                    if (Objects.nonNull(ifModifiedSinceString)) {
                        if (this.cacheManager.containsKey(id)) {
                            final long ifModifiedSince = request.getDateHeader("If-Modified-Since") / 1000L;
                            final long lastModified = this.cacheManager.getCreatedAt(id);
                            if (ifModifiedSince >= lastModified) {
                                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                return;
                            } else {
                                setLastModified(response, lastModified);
                            }
                        }
                    }
                }
                chain.doFilter(request, response);
                this.treatmentAfter(servletContext, response);
                return;
            default:
                chain.doFilter(request, response);
        }

    }

    private void treatmentAfter(final ServletContext servletContext, final HttpServletResponse response) {
        if (response.getStatus() == HttpServletResponse.SC_CREATED || response.getStatus() == HttpServletResponse.SC_FOUND) {
            this.convertDtoAndTreat(servletContext);
        }
    }

    private void convertDtoAndTreat(final ServletContext servletContext) {
        final Object dto = servletContext.getAttribute(ServletContextKey.DTO.name());
        if (dto instanceof OutBoardDto) {
            final OutBoardDto outBoardDto = (OutBoardDto) dto;
            this.cacheManager.put(outBoardDto.getId(), outBoardDto);
        }
        if (dto instanceof OutTaskDto) {
            final OutTaskDto outTaskDto = (OutTaskDto) dto;
            this.cacheManager.put(outTaskDto.getId(), outTaskDto);
        }
    }

    private void setLastModified(final HttpServletResponse response, final long lastModified) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZone(ZoneId.of("GMT"));
        final String formattedDate = formatter.format(Instant.ofEpochSecond(lastModified));
        response.setHeader("Last-Modified", formattedDate);
    }
}
