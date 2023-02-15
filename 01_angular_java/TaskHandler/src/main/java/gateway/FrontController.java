package gateway;

import utils.enumerations.UriControllerEnum;
import utils.helpers.UriHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@WebServlet(name="FrontServlet",loadOnStartup=1,urlPatterns={"/"})
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> optionalFirstUriPart = UriHelper.getFirstUriPart(request.getRequestURI());
        if(optionalFirstUriPart.isPresent()){
           this.dispatchToServlet(request, response, optionalFirstUriPart.get());
        }
    }

    private void dispatchToServlet(HttpServletRequest request, HttpServletResponse response, String firstUriPart) throws ServletException, IOException {
        Optional<UriControllerEnum> oCurrentUriControllerEnum = UriControllerEnum.fromString(firstUriPart);
        if(oCurrentUriControllerEnum.isPresent()){
            UriControllerEnum currentUriControllerEnum = oCurrentUriControllerEnum.get();
            switch (currentUriControllerEnum) {
                case USER:
                case BOARD:
                case TASK:
                case LOGIN:
                    this.forwardToServlet(request, response, UriHelper.convertStringToUri(currentUriControllerEnum.name()));
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        }
    }

    private void forwardToServlet(HttpServletRequest request, HttpServletResponse response, URI uri) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(uri.getPath());
        requestDispatcher.forward(request, response);
    }
}
