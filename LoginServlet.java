import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC credentials
    private String jdbcURL = "jdbc:mysql://localhost:3306/yourdatabase"; // Update with your database
    private String jdbcUsername = "root"; // Update with your DB username
    private String jdbcPassword = "password"; // Update with your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get email and password from the form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate email domain
        if (!email.endsWith("@srmist.edu.in")) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Email must be from the domain @srmist.edu.in');");
            out.println("location='login.html';");
            out.println("</script>");
            return;
        }

        // Connect to the database and check the credentials
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // SQL query to validate email and password
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // If user exists, redirect to index.html
                response.sendRedirect("index.html");
            } else {
                // If credentials are wrong, show error
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Invalid email or password');");
                out.println("location='login.html';");
                out.println("</script>");
            }

            // Close the resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Error connecting to the database');");
            out.println("location='login.html';");
            out.println("</script>");
        }
    }
}
