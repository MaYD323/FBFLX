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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "completecheckoutServelet",urlPatterns = "/api/completecheckout")
public class completecheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public completecheckoutServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUser = "CS122B";
        String loginPasswd = "CS122B";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
		String first = request.getParameter("first_name");
		String last = request.getParameter("last_name");
		String card_num = request.getParameter("card_num");
		String date = request.getParameter("date");
		Date date1 = null;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		java.sql.Date sDate = new java.sql.Date(date1.getTime());
        int count=0;

        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();

        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        		Statement statement = connection.createStatement();

        		String query = "SELECT * from creditcards where id = \""+card_num+"\" AND firstName = \""+first+"\" AND lastName =\""+last+"\" AND expiration = \""+sDate+"\"";

        		ResultSet resultSet = statement.executeQuery(query);

        		
        		// add a row for every star result
        		while (resultSet.next()) {

        			count+=1;

        		}
        		
        if (count!=0) {
            // Login succeeds
            // Set this user into current session
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            String email = ((User) ((HttpServletRequest) request).getSession().getAttribute("user")).getUsername();
            Map<String,Integer> l = (Map<String, Integer>) ((HttpServletRequest) request).getSession().getAttribute("checkcart");
            int userId = 0;
            try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();

        		
        		Statement Cstatement = connection.createStatement();

        		String Cquery = "SELECT id from customers where email = \""+email+"\"";

        		ResultSet CresultSet = Cstatement.executeQuery(Cquery);
        		
        		// add a row for every star result
        		while (CresultSet.next()) {

        			userId = CresultSet.getInt("id");

        		}
        		userId = 2;
        		for(Map.Entry<String, Integer> entry:l.entrySet()) {
        			String movieId = entry.getKey();
            		int num = entry.getValue();
            		for(int i=0;i<num;i++) {
            			Statement Sstatement = connection.createStatement();
            			String Squery = "insert into sales(customerId,movieId,saleDate) values("+userId+",\""+movieId+"\",curdate())";
            			Sstatement.executeUpdate(Squery);
            		}
        		}
        		
        } catch (Exception e) {

			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "DB error "+userId);
			response.getWriter().write(responseJsonObject.toString());


			response.setStatus(500);
        }
            
            
            
            
            
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            
            response.getWriter().write(responseJsonObject.toString());
        } else {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "Transaction error"+card_num+first+last+sDate);
            response.getWriter().write(responseJsonObject.toString());
        }
        } catch (Exception e) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			response.getWriter().write(jsonObject.toString());


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
