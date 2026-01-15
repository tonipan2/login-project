package org.example;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class Profile extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        UserDao userDao = new UserDaoImpl(MySQLDataSource.getDataSource());
        userService = new UserService(userDao);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String email = (String) req.getSession().getAttribute("email");
        if(email == null){
            resp.sendRedirect("/login.html");
            return;
        }

        User user = userService.findByEmail(email);
        if(user == null){
            resp.sendRedirect("/login.html?error=User not found");
            return;
        }

        String firstName = req.getParameter("firstName");
        String lastName  = req.getParameter("lastName");
        String newEmail  = req.getParameter("email");
        String password  = req.getParameter("password");

        if(firstName != null && !firstName.isBlank()) user.setFirstName(firstName);
        if(lastName != null && !lastName.isBlank()) user.setLastName(lastName);
        if(newEmail != null && !newEmail.isBlank()) user.setEmail(newEmail);
        if(password != null && !password.isBlank()) user.setPassword(userService.hashPassword(password));

        try {
            userService.updateProfile(user);
            resp.sendRedirect("/profile.html?success=1");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("/profile.html?error=" + e.getMessage());
        }
    }
}
