package gateway.controller;

import board.application.dto.InBoardDto;
import task.application.dto.InTaskDto;
import user.application.dto.InUserDto;
import user.application.helper.RequestHelper;
import utils.enumerations.UriControllerEnum;
import utils.helpers.UriHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static utils.enumerations.ServletNameEnum.*;

@WebServlet(name = "FrontServlet", loadOnStartup = 1, urlPatterns = {"/", "/*"})
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }

    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Optional<String> optionalFirstUriPart = UriHelper.getUriServletPart(uri);
        if (optionalFirstUriPart.isPresent()) {
            this.dispatchToServlet(request, response, optionalFirstUriPart.get(), uri);
        }
    }

    private void dispatchToServlet(final HttpServletRequest request, final HttpServletResponse response, final String firstUriPart, final String uri) throws ServletException, IOException {
        Optional<UriControllerEnum> oCurrentUriControllerEnum = UriControllerEnum.fromString(firstUriPart.toUpperCase());
        if (oCurrentUriControllerEnum.isPresent()) {
            UriControllerEnum currentUriControllerEnum = oCurrentUriControllerEnum.get();
            switch (currentUriControllerEnum) {
                case USER:
                    this.setRequestAttributes(request, uri, InUserDto.class);
                    this.forwardToServlet(request, response, USER_SERVLET.toString());
                    break;
                case BOARD:
                    this.setRequestAttributes(request, uri, InBoardDto.class);
                    this.forwardToServlet(request, response, BOARD_SERVLET.toString());
                    break;
                case TASK:
                    this.setRequestAttributes(request, uri, InTaskDto.class);
                    this.forwardToServlet(request, response, TASK_SERVLET.toString());
                    break;
                case LOGIN:
                    this.forwardToServlet(request, response, LOGIN_SERVLET.toString());
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        }
    }

    private void setRequestAttributes(final HttpServletRequest request, final String uri, final Class dtoClass) throws IOException {
        request.setAttribute("parameter", UriHelper.getUriParameterPart(uri));
        request.setAttribute("dto", RequestHelper.getDtoFromRequestBody(request, dtoClass));
    }

    private void forwardToServlet(final HttpServletRequest request, final HttpServletResponse response, final String servletName) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getServletContext().getNamedDispatcher(servletName);
        requestDispatcher.forward(request, response);
    }
}
