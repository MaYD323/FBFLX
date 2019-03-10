import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.io.IOException;
import java.io.PrintWriter;
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
        // getting started with reCAPTCHA
    	String userAgent = request.getHeader("User-Agent");
    	
    	if (userAgent != null && !userAgent.contains("Android")) {
	        PrintWriter out = response.getWriter();
	
	        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
	        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
	
	        // Verify reCAPTCHA
	        try {
	            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
	        } catch (Exception e) {
	       	    JsonObject responseJsonObject = new JsonObject();
	            responseJsonObject.addProperty("message", e.getMessage());
	            response.getWriter().write(responseJsonObject.toString());
	            return;
	        }
	        // ending with started with reCAPTCHA
    	}
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = "";
        String pwd = "";
        try {

        	Context initCtx = new InitialContext();
        	
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            Connection connection = ds.getConnection();
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
        if (username.equals(email) &&new StrongPasswordEncryptor().checkPassword(password, pwd)) {
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
