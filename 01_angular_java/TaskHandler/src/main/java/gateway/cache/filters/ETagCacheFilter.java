package gateway.cache.filters;

import gateway.cache.model.EtagCacheManager;
import org.picocontainer.MutablePicoContainer;
import user.application.dto.OutUserDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ETagCacheFilter extends HttpFilter {

    private EtagCacheManager<String, String> cacheManager;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        final ServletContext servletContext = filterConfig.getServletContext();
        final MutablePicoContainer globalContainer = (MutablePicoContainer) ServletContextHelper.getAttribute(servletContext, ServletContextKey.GLOBAL_CONTAINER);
        this.cacheManager = (EtagCacheManager<String, String>) globalContainer.getComponent(EtagCacheManager.class);
    }

    @Override
    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        this.cacheManager.clean();
        final String methodHTTP = request.getMethod();
        final ServletContext servletContext = request.getServletContext();
        final String[] urlParts = RequestUtils.getUrlParts(request);
        switch(methodHTTP){
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
                if(urlParts.length > 1){
                    final String ifNoneMatchHeader = request.getHeader("If-None-Match");
                    if (Objects.nonNull(ifNoneMatchHeader)) {
                        final String id = urlParts[1];
                        if (this.cacheManager.containsKey(id)) {
                            final String[] etagValues = ifNoneMatchHeader.split(",");
                            final List<String> etagList = Arrays.stream(etagValues).collect(Collectors.toList());
                            final String existingEtag = this.cacheManager.getETag(id);
                            if (etagList.contains(existingEtag)) {
                                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                return;
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

    private void treatmentAfter(final ServletContext servletContext, final HttpServletResponse response){
        if(response.getStatus() == HttpServletResponse.SC_CREATED || response.getStatus() == HttpServletResponse.SC_FOUND){
            this.convertDtoAndTreat(servletContext, response);
        }
    }

    private void convertDtoAndTreat(final ServletContext servletContext, final HttpServletResponse response){
        final Object dto = servletContext.getAttribute(ServletContextKey.DTO.name());
        if(dto instanceof OutUserDto){
            final OutUserDto outUserDto = (OutUserDto) dto;
            this.cacheManager.put(outUserDto.getId(), response.getHeader("ETag"));
        }
    }



}
