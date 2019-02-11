import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class MovieServelet
 */
@WebServlet(name = "BrowsingServelet",urlPatterns = "/api/browsing")
public class BrowsingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowsingServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUser = "122b";
        String loginPasswd = "122b";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        response.setContentType("application/json"); 

        PrintWriter out = response.getWriter();

        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();

        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        		Statement statement = connection.createStatement();

        		String query = "SELECT * from genres";

        		ResultSet genreSet = statement.executeQuery(query);
        		JsonArray genreArray = new JsonArray();

        		
        		// add a row for every star result
        		while (genreSet.next()) {

        			String genreName = genreSet.getString("name");
        			
                    JsonObject genreObject = new JsonObject();
                    genreObject.addProperty("name", genreName);

                    genreArray.add(genreObject);

        		}
        		

                out.write(genreArray.toString());
                response.setStatus(200);
        		genreSet.close();
        		statement.close();
        		connection.close();
        		
        } catch (Exception e) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());


			response.setStatus(500);
        }

        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
