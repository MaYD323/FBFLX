import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String loginUser = "CS122B";
        String loginPasswd = "CS122B";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        String email = "";
        String pwd = "";
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		String query = "select * from customers c where email = ?";
    		PreparedStatement statement = connection.prepareStatement(query);
    		statement.setString(1, username);
    		ResultSet rs = statement.executeQuery();
    		while(rs.next())
    		{
    			email = rs.getString("email");
    			pwd = rs.getString("password");
    		}
        }
        catch(Exception e)
        {
        	 JsonObject responseJsonObject = new JsonObject();
             responseJsonObject.addProperty("status", "fail");
             responseJsonObject.addProperty("message", "DB error");
             response.getWriter().write(responseJsonObject.toString());
        }

        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
        if (username.equals(email) && password.equals(pwd)) {
            // Login succeeds
            // Set this user into current session
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(username));

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            if (!username.equals(email)) {
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            } else {
                responseJsonObject.addProperty("message", "Incorrect Password");
            }
            response.getWriter().write(responseJsonObject.toString());
        }
    }
}
