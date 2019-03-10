import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

		
        response.setContentType("application/json"); 

        PrintWriter out = response.getWriter();

        
        
        try {

        		
	        	Context initCtx = new InitialContext();
	        	
	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                out.println("envCtx is NULL");
	
	            // Look up our data source
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
	            Connection connection = ds.getConnection();
        		String query = "SELECT * from genres";
        		PreparedStatement statement = connection.prepareStatement(query);
        		ResultSet genreSet = statement.executeQuery();
        		
        		
        		
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
