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
    String USERNAME="test";
    String PASSWORD="test";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean isAuthenticated = USERNAME.equals(username) && PASSWORD.equals(password);

        if(isAuthenticated){
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"isAuthenticated\": " + isAuthenticated + "}");
    }
}
