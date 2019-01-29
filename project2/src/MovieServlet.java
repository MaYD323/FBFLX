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
@WebServlet(name = "MovieServelet",urlPatterns = "/api/movies")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
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
        response.setContentType("application/json"); 
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String star = request.getParameter("star");
		String sorting = request.getParameter("sorting");
		String npp = request.getParameter("npp");
		String page = request.getParameter("page");
		String genre = request.getParameter("genre");
		String st = request.getParameter("st");
        PrintWriter out = response.getWriter();

        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();

        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        		Statement statement = connection.createStatement();
        		Statement countStatement = connection.createStatement();
        		String tableQuery = "SELECT M.title, M.director, M.year, M.id, rating from movies M, ratings R";
        		String selectQuery = " where M.id = R.movieId";
        		String countQuery = "SELECT COUNT(*) as ct from movies M, ratings R";
        		if(title.length()>0)
        		{
        			selectQuery+=" AND M.title like \'%"+title+"%\'";
        		}
        		if(st.length()>0)
        		{
        			selectQuery+=" AND M.title like \'"+st+"%\'";
        		}
        		if(year.length()>0)
        		{
        			selectQuery+=" AND M.year = "+year;
        		}
        		if(director.length()>0)
        		{
        			selectQuery+=" AND M.director like \'%"+director+"%\'";
        		}
        		if(star.length()>0)
        		{
        			tableQuery+= ", stars_in_movies SIM, stars S";
        			countQuery+= ", stars_in_movies SIM, stars S";
        			selectQuery+=" AND SIM.movieId = M.id AND SIM.starId = S.id AND S.name like \'%"+star+"%\'";
        		}
        		if(genre.length()>0)
        		{
        			tableQuery+= ", genres_in_movies GIM, genres G";
        			countQuery+= ", genres_in_movies GIM, genres G";
        			selectQuery+=" AND GIM.movieId = M.id AND GIM.genreId = G.id AND G.name like \'%"+genre+"%\'";
        		}
        		if(sorting.equals("rating"))
        		{
        			selectQuery+=" ORDER BY "+sorting+" DESC";
        		}
        		else {
        			selectQuery+=" ORDER BY "+sorting+" ASC";
        		}
        		String pageQuery=" limit "+npp;
        		pageQuery+=" OFFSET ";
        		pageQuery+=Integer.parseInt(npp)*(Integer.parseInt(page)-1);
        		countQuery += selectQuery;
        		selectQuery+=pageQuery;
        		String query = tableQuery+selectQuery;
        		ResultSet countSet = countStatement.executeQuery(countQuery);
        		ResultSet movieSet = statement.executeQuery(query);
        		JsonArray movieArray = new JsonArray();
        		int count = 0;
        		while(countSet.next())
        		{
        			count = countSet.getInt("ct");
        		}
        		int totalPage = (int) Math.ceil((double)count/Integer.parseInt(npp));

        		
        		// add a row for every star result
        		while (movieSet.next()) {

        			String movieId = movieSet.getString("id");
        			String movieTitle = movieSet.getString("title");
        			int movieYear = movieSet.getInt("year");
        			String movieDirector = movieSet.getString("director");
        			float movieRating = movieSet.getFloat("rating");
                    JsonObject movieObject = new JsonObject();
                    movieObject.addProperty("movieId", movieId);
                    movieObject.addProperty("movieTitle", movieTitle);
                    movieObject.addProperty("movieYear", movieYear);
                    movieObject.addProperty("movieDirector", movieDirector);
                    movieObject.addProperty("movieRating", movieRating);
        			String starQuery = "select S.name, S.id from stars_in_movies SIM, stars S where SIM.movieId = \'"+movieId+"\' AND SIM.starId = S.id";
        			Statement starStatement = connection.createStatement();
        			ResultSet starSet = starStatement.executeQuery(starQuery);

        			JsonArray starArray = new JsonArray();
        			while(starSet.next())
        			{
        				String name = starSet.getString("name");
        				String starId = starSet.getString("id");
        				JsonObject starObject = new JsonObject();
        				starObject.addProperty("name",name);
        				starObject.addProperty("starId",starId);
        				starArray.add(starObject);

        			}
        			movieObject.add("stars", starArray);

        			starSet.close();
        			starStatement.close();
        			String genreQuery = "select G.name from genres_in_movies GIM, genres G where GIM.movieId = \'"+movieId+"\' AND GIM.genreId = G.id";
        			Statement genreStatement = connection.createStatement();
        			ResultSet genreSet = genreStatement.executeQuery(genreQuery);

        			JsonArray genreArray = new JsonArray();
        			while(genreSet.next())
        			{
        				String name = genreSet.getString("name");
        				JsonObject genreObject = new JsonObject();
        				genreObject.addProperty("name",name);
        				genreArray.add(genreObject);

        			}
        			movieObject.add("genre", genreArray);
        			movieObject.addProperty("urlTitle",title);
        			movieObject.addProperty("urlYear",year);
        			movieObject.addProperty("urlDirector",director);
        			movieObject.addProperty("urlStar",star);
        			movieObject.addProperty("count",count);
        			movieObject.addProperty("npp",npp);
        			movieObject.addProperty("sorting",sorting);
        			movieObject.addProperty("page",page);
        			movieObject.addProperty("totalPage",totalPage);
        			movieObject.addProperty("genreWord",genre);
        			movieObject.addProperty("st",st);
        			genreSet.close();
        			genreStatement.close();
        			movieArray.add(movieObject);

        		}
        		

                out.write(movieArray.toString());
                response.setStatus(200);
        		movieSet.close();
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
