package gateway.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="LoginServlet",urlPatterns={"/login"})
public class LoginController extends HttpServlet {
    private final String USERNAME="test";
    private final String PASSWORD="test";

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final boolean isAuthenticated = USERNAME.equals(username) && PASSWORD.equals(password);

        if(isAuthenticated){
            final HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"isAuthenticated\": " + isAuthenticated + "}");
    }
}
