import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.CallableStatement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "makechangeServlet", urlPatterns = "/api/makechange")
public class makechange extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static int status=0;

    /**
     * handles POST requests to store session information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        Long lastAccessTime = session.getLastAccessedTime();
        
        response.setContentType("application/json");
        
        JsonArray JA = new JsonArray();
        
        //**************************************************************************************

        String loginUser = "122b";
        String loginPasswd = "122b";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?noAccessToProcedureBodies = true";

		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();

    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

			String query = "Select TABLE_NAME " + 
					"From INFORMATION_SCHEMA.TABLES " + 
					"Where TABLE_SCHEMA= 'moviedb';";

			PreparedStatement statement = connection.prepareStatement(query);

			ResultSet rs = statement.executeQuery();
			
			JsonArray tableArray = new JsonArray();
//			//JsonArray genreArray = new JsonArray();

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				
				String queryNext = "Select COLUMN_NAME, DATA_TYPE,IS_NULLABLE " + 
						"From INFORMATION_SCHEMA.COLUMNS " + 
						"Where TABLE_NAME Like ?;";
				
				PreparedStatement statementNext = connection.prepareStatement(queryNext);
				
				statementNext.setString(1, tableName);
				
				ResultSet result = statementNext.executeQuery();
				
				
				JsonObject tableObject = new JsonObject();
				tableObject.addProperty("name",tableName);
				JsonArray columArray = new JsonArray();
				while(result.next()) {
					JsonObject columnObject = new JsonObject();
					columnObject.addProperty("cname", result.getString("COLUMN_NAME"));
					columnObject.addProperty("ctype", result.getString("DATA_TYPE"));
					columArray.add(columnObject);
					
				}
				tableObject.add("column",columArray);
				String name = request.getParameter("name");
				String birth = request.getParameter("birth");
				if(status==1) {
					tableObject.addProperty("error-message","successfully added "+name);
				}
				else if(status==2) {
					tableObject.addProperty("error-message","failed to add, insertion error");
				}
				else if(status==3) {
					tableObject.addProperty("error-message","failed to add, nothing is entered");
				}
				else {
					tableObject.addProperty("error-message","");
				}
				tableArray.add(tableObject);
				
			}
			
			JA.add(tableArray);
			
	        
		} catch (Exception e) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			response.getWriter().write(jsonObject.toString());


			response.setStatus(500);
		}
        //**************************************************************************************
		response.getWriter().write(JA.toString());
        // write all the data into the jsonObject
//        response.getWriter().write(responseJsonObject.toString());
    }

    /**
     * handles GET requests to add and show the item list information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	//insert here
		String name = request.getParameter("name");
		String birth = request.getParameter("birth");
        String loginUser = "root";
        String loginPasswd = "qweasdzxc";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?noAccessToProcedureBodies = true";

		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();

    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        // get the ID and password for each customer
    		if(name!=null && name!="") {
	    		CallableStatement insert_star = connection.prepareCall("{call insert_star(?,?)}");
	    		insert_star.setString(1, name);
	    		if(birth.length()>0) {
	    			insert_star.setInt(2, Integer.parseInt(birth));
	    		}
	    		else {
	    			insert_star.setNull(2, java.sql.Types.BLOB);
	    		}
				insert_star.execute();
				status=1;
    		}
    		else {
    			if(name!=null) {
    				status=3;
    			}
    			else {
    				status=4;
    			}
    		}
		} catch (Exception e) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			response.getWriter().write(jsonObject.toString());
			response.setStatus(500);
			status=2;
		}
    	doPost(request, response);
    }
}
