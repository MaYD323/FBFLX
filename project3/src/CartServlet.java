

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class AddServlet
 */
@WebServlet(name = "CartServlet", urlPatterns="/api/cart")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String item = request.getParameter("item");
        response.setContentType("application/json"); 
        HttpSession session = request.getSession();
        JsonArray cartJsonArray = new JsonArray();
        PrintWriter out = response.getWriter();
        String loginUser = "122b";
        String loginPasswd = "122b";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            previousItems.add(item);
            session.setAttribute("previousItems", previousItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {
                previousItems.add(item);
            }
        }
        Map<String,Integer> l = new HashMap<String,Integer>();
        for(String it:previousItems) {
        	if(l.get(it)==null) {
        		l.put(it,1);
        	}
        	else {
        		int z = l.get(it)+1;
        		l.replace(it,z);
        	}
        }
        for(Map.Entry<String, Integer> entry:l.entrySet()) {
        	String id = entry.getKey();
        	int number = entry.getValue();
        	String title = "";
        	try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
	    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

	    		Statement statement = connection.createStatement();
	    		String query = "SELECT M.title from movies M where M.id = \""+id+"\"";
	    		ResultSet movieSet = statement.executeQuery(query);
	    		while(movieSet.next())
	    		{
	    			title = movieSet.getString("title");
	    		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	JsonObject movie = new JsonObject();
        	movie.addProperty("id",id);
        	movie.addProperty("title",title);
        	movie.addProperty("number",number);
        	cartJsonArray.add(movie);
        }

        out.write(cartJsonArray.toString());

        response.setStatus(200);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
