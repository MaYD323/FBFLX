import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class singleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); 
		String id = request.getParameter("id");;


        PrintWriter out = response.getWriter();
        String loginUser = "CS122B";
        String loginPasswd = "CS122B";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();

    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

			String query = "SELECT name, starId, sim.movieId, rating, director, year, title from stars as s, stars_in_movies as sim,  movies as m, ratings as r where  sim.starId = s.id and sim.movieId = m.id and r.movieId = m.id and m.id = ?";
			String genreQuery = "SELECT * from genres_in_movies as gim, movies as m, genres as g where g.id=gim.genreId and m.id = gim.movieId and m.id = ?";

			PreparedStatement statement = connection.prepareStatement(query);
			PreparedStatement genreStatement = connection.prepareStatement(genreQuery);


			statement.setString(1, id);
			genreStatement.setString(1, id);

			ResultSet rs = statement.executeQuery();
			ResultSet gs = genreStatement.executeQuery();
			JsonArray starArray = new JsonArray();
			JsonArray genreArray = new JsonArray();


			while (gs.next()) {
				JsonObject g = new JsonObject();
				String name = gs.getString("name");
				String gid = gs.getString("genreId");
				g.addProperty("name",name);
				g.addProperty("id",gid);
				genreArray.add(g);
			}
			while (rs.next()) {

				String starId = rs.getString("starId");
				String starName = rs.getString("name");
				String movieId = rs.getString("movieId");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");
				float rating = rs.getFloat("rating");



				JsonObject starObject = new JsonObject();
				starObject.addProperty("starName", starName);
				starObject.addProperty("starId", starId);
				starObject.addProperty("movieId", movieId);
				starObject.addProperty("movieTitle", movieTitle);
				starObject.addProperty("movieYear", movieYear);
				starObject.addProperty("movieDirector", movieDirector);
				starObject.addProperty("rating", rating);
				starObject.add("genres",genreArray);
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