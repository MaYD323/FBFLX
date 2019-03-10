import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

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
@WebServlet(name = "AutoServlet", urlPatterns = "/api/auto")
public class AutoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");

		response.setContentType("application/json"); 


        PrintWriter out = response.getWriter();
        JsonArray movieArray = new JsonArray();
        try {
			Context initCtx = new InitialContext();
			
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            Connection connection = ds.getConnection();
			String query="";
			if(title.length()>2) {
				query = "select M.id, M.title from movies M WHERE match(M.title) against(";
				String [] wordList = title.split(" ");
				for(int i=0;i<wordList.length;i++) {
					query+="? ";
				}
				query+=" IN BOOLEAN MODE) limit 10";
				PreparedStatement statement = connection.prepareStatement(query);
				for(int i=0;i<wordList.length;i++) {
					statement.setString(i+1,"+"+wordList[i]+"*");
				}
				ResultSet rs = statement.executeQuery();
				while(rs.next()) {
					JsonObject movieObject = new JsonObject();
					String movieId = rs.getString("id");
        			String movieTitle = rs.getString("title");
					movieObject.addProperty("movieId", movieId);
                    movieObject.addProperty("value", movieTitle);
                    movieArray.add(movieObject);
				}
				rs.close();
				statement.close();
				
			}
			out.write(movieArray.toString());
            response.setStatus(200);
    		connection.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());


			response.setStatus(500);
		}
        /**
         * This example only allows username/password to be anteater/123456
         * In real world projects, you should talk to the database to verify username/password
         */
            // Login succeeds
            // Set this user into current session

    }
}
