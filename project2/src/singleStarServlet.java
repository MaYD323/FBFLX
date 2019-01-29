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


@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/single-star")
public class singleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");


		String id = request.getParameter("id");
		


        PrintWriter out = response.getWriter();
        String loginUser = "CS122B";
        String loginPasswd = "CS122B";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();

    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

			String query = "SELECT * from stars as s, stars_in_movies as sim, movies as m where m.id = sim.movieId and sim.starId = s.id and s.id = ?";


			PreparedStatement statement = connection.prepareStatement(query);


			statement.setString(1, id);


			ResultSet rs = statement.executeQuery();

			JsonArray starArray = new JsonArray();
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String star = request.getParameter("star");
			String sorting = request.getParameter("sorting");
			String npp = request.getParameter("npp");
			String page = request.getParameter("page");
			String genre = request.getParameter("genre");
			String st = request.getParameter("st");


			while (rs.next()) {

				String starId = rs.getString("starId");
				String starName = rs.getString("name");
				String starDob = rs.getString("birthYear");

				String movieId = rs.getString("movieId");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");



				JsonObject starObject = new JsonObject();
				starObject.addProperty("starId", starId);
				starObject.addProperty("starName", starName);
				starObject.addProperty("starDob", starDob);
				starObject.addProperty("movieId", movieId);
				starObject.addProperty("movieTitle", movieTitle);
				starObject.addProperty("movieYear", movieYear);
				starObject.addProperty("movieDirector", movieDirector);
				starObject.addProperty("urlTitle",title);
				starObject.addProperty("urlYear",year);
				starObject.addProperty("urlDirector",director);
				starObject.addProperty("urlStar",star);
				starObject.addProperty("npp",npp);
				starObject.addProperty("sorting",sorting);
				starObject.addProperty("page",page);
				starObject.addProperty("genreWord",genre);
				starObject.addProperty("st",st);

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