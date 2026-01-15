package org.example;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.init(org.example.MySQLDataSource.getDataSource());

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/resources/frontend");
        context.addServlet(DefaultServlet.class, "/");
        context.addServlet(Register.class, "/register");
        context.addServlet(Login.class, "/login");
        context.addServlet(Profile.class, "/profile");

        server.setHandler(context);
        server.start();
        server.join();
    }
}