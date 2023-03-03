package gateway.controller;

import board.application.dto.InBoardDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import task.application.dto.InTaskDto;
import user.application.dto.InUserDto;
import user.application.helper.RequestHelper;
import utils.enumerations.UriControllerEnum;
import utils.helpers.LoggerHelper;
import utils.helpers.UriHelper;
import utils.metric.PrometheusMetricRegistry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static utils.enumerations.ServletNameEnum.*;

@WebServlet(name = "PrometheusServlet")
public class PrometheusController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().write(PrometheusMetricRegistry.getInstance().getRegistry().scrape());
    }

}
