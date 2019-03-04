import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JsonArray responseJsonArray = new JsonArray();
    	response.setContentType("application/json"); 
    	Enumeration<String> params = request.getParameterNames();
    	HttpSession session = request.getSession();
    	User u = (User)session.getAttribute("user");
    	String username = u.getUsername();
    	PrintWriter out = response.getWriter();
    	Map<String,Integer> l = new HashMap<String,Integer>();
    	while(params.hasMoreElements())
        {
    	  String paramName = (String)params.nextElement();
          int value = Integer.parseInt(request.getParameter(paramName));
          JsonObject item = new JsonObject();
          l.put(paramName,value);
        }
    	session.setAttribute("checkcart", l);
    	for(Map.Entry<String, Integer> entry:l.entrySet()) {
    		String name = entry.getKey();
    		int value = entry.getValue();
    		JsonObject jo = new JsonObject();
    		jo.addProperty("name",name);
    		jo.addProperty("num",value);
    		jo.addProperty("username",username);
    		responseJsonArray.add(jo);
    	}
    	out.write(responseJsonArray.toString());

        response.setStatus(200);
    	
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
