import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



@WebServlet(name = "StarsServlet", urlPatterns = "/api/stars")
public class StarsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
    		Statement statement = connection.createStatement();

            String query = "SELECT * from stars";


            ResultSet rs = statement.executeQuery(query);

            JsonArray starArray = new JsonArray();

            while (rs.next()) {
                String star_id = rs.getString("id");
                String star_name = rs.getString("name");
                String star_dob = rs.getString("birthYear");


                JsonObject starObject = new JsonObject();
                starObject.addProperty("starId", star_id);
                starObject.addProperty("starName", star_name);
                starObject.addProperty("starDob", star_dob);

                starArray.add(starObject);
            }
            

            out.write(starArray.toString());

            response.setStatus(200);

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
        	

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());


			response.setStatus(500);

        }
        out.close();

    }
}
