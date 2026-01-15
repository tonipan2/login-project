package org.example;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/register")
public class Register extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        UserDao userDao = new UserDaoImpl(org.example.MySQLDataSource.getDataSource());
        userService = new UserService(userDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String firstName = req.getParameter("firstName");
        String lastName  = req.getParameter("lastName");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");

        if (firstName == null || lastName == null || email == null || password == null ||
                firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            resp.sendRedirect("/register.html?error=" + URLEncoder.encode("All fields are required!", StandardCharsets.UTF_8));
            return;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userService.registerUser(user);
            resp.sendRedirect("/login.html?success=1");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("/register.html?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

}
