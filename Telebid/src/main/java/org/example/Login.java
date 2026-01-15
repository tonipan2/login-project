package org.example;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        UserDao userDao = new UserDaoImpl(MySQLDataSource.getDataSource());
        userService = new UserService(userDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (userService.authenticate(email, password)) {
            req.getSession().setAttribute("email", email);
            resp.sendRedirect("/profile.html");
        } else {
            resp.sendRedirect("/login.html?error=1");
        }
    }
}
