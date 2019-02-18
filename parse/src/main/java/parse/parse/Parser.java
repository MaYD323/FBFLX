package parse.parse;
import java.io.IOException;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	public HashMap<String, Star> starsToInsert;
	public HashMap<String,Movie> moviesToInsert;
	public HashMap<String,ArrayList<String>> SIMToInsert;
	Document movie;
	Document SIM;
	Document star;
	public Parser() {
		starsToInsert = new HashMap<String,Star>();
		moviesToInsert = new HashMap<String,Movie>();
		SIMToInsert = new HashMap<String, ArrayList<String>>();
	}
	
	
	private void parseXmlFile() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            movie = db.parse("mains243.xml");
            star = db.parse("actors63.xml");
            SIM = db.parse("casts124.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}
	private String getTextValue(Element ele, String tagName) {
		try {
			String textVal = null;
	        NodeList nl = ele.getElementsByTagName(tagName);
	        if (nl != null && nl.getLength() > 0) {
	            Element el = (Element) nl.item(0);
	            textVal = el.getFirstChild().getNodeValue();
	        	}
        return textVal;
    		}
    	catch(Exception e) {
    		return "NULL";
    		}
    }
	
	private int getIntValue(Element ele, String tagName) {
		try {
			return Integer.parseInt(getTextValue(ele, tagName));
			}
		catch(Exception e) {
			return 0;
		}
    }
	
	//Parse Movie
	private Movie getMovie(Element movEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        String id = getTextValue(movEl, "fid");
        String title = getTextValue(movEl, "t");
        int year = getIntValue(movEl, "year");
        String director = "";
        ArrayList<String> genres = new ArrayList<String>();
        NodeList dirList = movEl.getElementsByTagName("dirs");
        if(dirList!=null && dirList.getLength()>0)
        {
        	Element dirEl = (Element) dirList.item(0);
        	NodeList firstDir = dirEl.getElementsByTagName("dir");
        	if(firstDir!=null&&dirList.getLength()>0)
        	{
        		Element dirEl1 = (Element)firstDir.item(0);
        		director = getTextValue(dirEl1,"dirn");
        	}
        }
        NodeList genreList  = movEl.getElementsByTagName("cats");
        if(genreList!=null && genreList.getLength()>0) {
        	for(int i=0;i<genreList.getLength();i++)
        	{
        		Element genreEl = (Element)genreList.item(i);
        		genres.add ( getTextValue(genreEl,"cat") );
        	}
        }

        //Create a new Employee with the value read from the xml nodes
        Movie movie = new Movie(id, title, year, director, genres);

        return movie;
    }
	
	private void parseMovie() {
        //get the root elememt
        Element docEle = movie.getDocumentElement();

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("film");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                Movie movie = getMovie(el);

                //add it to list
                moviesToInsert.put(getTextValue(el,"fid"), movie);
            }
        }
    }
	//Parse Star
	private Star getStar(Element starEl)
	{
		String name = getTextValue(starEl,"stagename");
		int year = getIntValue(starEl,"dob");
		
		Star result = new Star(name,year);
		
		return result;
	}
	private void parseActors() {
		Element docEle = star.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("actor");
		if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                Star result = getStar(el);
                starsToInsert.put(result.getName(),result);
            }
        }
	}
	
	//Parse SIM(Cast)
	private void parseCasts() {
		Element SIMEle = SIM.getDocumentElement();
		NodeList castList = SIMEle.getElementsByTagName("filmc");
		if (castList != null && castList.getLength() > 0) {
			for (int i = 0; i < castList.getLength(); i++) {
                Element el = (Element) castList.item(i);
                NodeList actor = el.getElementsByTagName("m");
                for(int z=0;z<actor.getLength();z++) {
                	Element ele  = (Element) actor.item(z);
                	String filmId = getTextValue(ele,"f");
                	String actorName = getTextValue(ele,"a");
                	if(!actorName.equals("s a")) {
                		if(SIMToInsert.containsKey(filmId)) {
                			ArrayList<String> actorList = SIMToInsert.get(filmId);
                			if(!actorList.contains(actorName)) {
                				actorList.add(actorName);
                				SIMToInsert.put(filmId,actorList);
                			}
                		}
                		else {
                			ArrayList<String> actorList = new ArrayList<String>();
                			actorList.add(actorName);
                			SIMToInsert.put(filmId,actorList);
                		}
                	}
                }
			}
		}
	}
	//Insert into DB
	private void insertToDb()
	{
		String loginUser = "122b";
        String loginPasswd = "122b";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
        	HashMap<String,String> allMovies = new HashMap<String,String>();
        	HashMap<String,String> allStars = new HashMap<String,String>();
        	HashMap<String,Integer> allGenres = new HashMap<String,Integer>();
        	ArrayList<String> allSIM = new ArrayList<String>();
        	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			String allMQ = "select distinct(title),id from movies;";
			PreparedStatement allMS = connection.prepareStatement(allMQ);
			ResultSet allMset = allMS.executeQuery();
			while(allMset.next())
        	{
				allMovies.put(allMset.getString("title"),allMset.getString("id"));
        	}
			allMset.close();
			allMS.close();
			
			String allSQ = "select distinct(name),id from stars;";
			PreparedStatement allSS = connection.prepareStatement(allSQ);
			ResultSet allSset = allSS.executeQuery();
			while(allSset.next())
        	{
				allStars.put(allSset.getString("name"),allSset.getString("id"));
        	}
			allSset.close();
			allSS.close();
			
			String allGQ = "select distinct(name),id from genres;";
			PreparedStatement allGS = connection.prepareStatement(allGQ);
			ResultSet allGset = allGS.executeQuery();
			while(allGset.next())
        	{
				allGenres.put(allGset.getString("name"),allGset.getInt("id"));
        	}
			allGset.close();
			allGS.close();
			
			String allSIMQ = "select starId,movieId from stars_in_movies;";
			PreparedStatement allSIMS = connection.prepareStatement(allSIMQ);
			ResultSet allSIMset = allSIMS.executeQuery();
			while(allSIMset.next())
        	{
				allSIM.add(allSIMset.getString("starId")+"-"+allSIMset.getString("movieId"));
        	}
			allSIMset.close();
			allSIMS.close();
			
			
			int genreId = 100;
			Statement gidS = connection.createStatement();
			ResultSet gmax = gidS.executeQuery("select max(id)+1 as mx from genres");
			while(gmax.next()) {
				genreId = gmax.getInt("mx")+1;
			}
			gmax.close();
			gidS.close();
			
			int max_star = 10000;
			Statement sidS = connection.createStatement();
			ResultSet smax = sidS.executeQuery("select substring_index(max(id),'m',-1) as mx from stars;");
			while(smax.next()) {
				max_star = Integer.parseInt(smax.getString("mx"))+1;
			}
			sidS.close();
			smax.close();
			
			int max_movie = 10000000;
			Statement midS = connection.createStatement();
			ResultSet mmax = midS.executeQuery("select substring_index(max(id),'t',-1) as mx from movies;");
			while(mmax.next()) {
				max_movie = Integer.parseInt(mmax.getString("mx"))+1;
			}
			midS.close();
			mmax.close();
			
        	System.out.println("Number of stars "+starsToInsert.size());
        	System.out.println("Number of movies "+moviesToInsert.size());
        	System.out.println("Number of casts "+SIMToInsert.size());
        	int[] movieInserted;
        	int[] starInserted;
        	int[] genreInserted;
        	int[] SIMInserted;
        	int[] GIMInserted;
        	int[] RInserted;
        	
        	String insertMQ="insert into movies values(?,?,?,?)";
        	String insertSQ = "insert into stars values(?,?,?)";
        	String insertGQ = "insert into genres values(?,?)";
        	String insertSIMQ = "insert into stars_in_movies values(?,?)";
        	String insertGIMQ = "insert into genres_in_movies values(?,?)";
        	String insertR = "insert into ratings values(?,0,0)";
        	connection.setAutoCommit(false);
        	try {
        		PreparedStatement s1=connection.prepareStatement(insertMQ);
        		PreparedStatement s2=connection.prepareStatement(insertSQ);
        		PreparedStatement s3=connection.prepareStatement(insertGQ);
        		PreparedStatement s4=connection.prepareStatement(insertSIMQ);
        		PreparedStatement s5=connection.prepareStatement(insertGIMQ);
        		PreparedStatement s6=connection.prepareStatement(insertR);
        		int movie_count = 0;
        		int genre_count=0;
        		Iterator mit = moviesToInsert.entrySet().iterator();
        		while(mit.hasNext()) {
        			Map.Entry moviePair = (Map.Entry)mit.next();
        			Movie m = ((Movie)moviePair.getValue());
        			if(!allMovies.containsKey(m.getTitle())&&m.getTitle()!=null && !m.getTitle().equals("NULL")) {
        				String newMId = "tt"+(movie_count+max_movie);
        				String mTitle = "NULL";
    	            	if(m.getTitle()!=null)
    	            		mTitle = m.getTitle();
    	            	int mYear = m.getYear();
    	            	String mDirector = "NULL";
    	            	if(m.getDirector()!=null)
    	            		mDirector = m.getDirector();
    	            	s1.setString(1, newMId);
    	            	s1.setString(2, mTitle);
    	            	s1.setInt(3, mYear);
    	            	s1.setString(4, mDirector);
    	            	movie_count++;
    	            	s1.addBatch();
    	            	s6.setString(1, newMId);
    	            	s6.addBatch();
    	            	allMovies.put(mTitle,newMId);
    	            	for(int i=0;i<m.genre.size();i++) {
    	            		
    	            		String gName = m.genre.get(i);
    	            		if(!allGenres.containsKey(gName)&&gName!=null) {
    	            			int newGId = genre_count + genreId;
    	            			s3.setInt(1, newGId);
    	            			s3.setString(2, gName);
    	            			s3.addBatch();
    	            			genre_count++;
    	            			s5.setInt(1, newGId);
    	            			s5.setString(2,newMId);
    	            			s5.addBatch();
    	            			allGenres.put(gName,newGId);  
    	            		}
    	            		else {
    	            			if(gName!=null) {
        	            			s5.setInt(1, allGenres.get(gName));
        	            			s5.setString(2, newMId);
        	            		    s5.addBatch();
    	            			}
    	            		}
    	            	
    	            	}
        			}
        		}	
        	Iterator sit = starsToInsert.entrySet().iterator();
        	int star_count=0;
        	while(sit.hasNext()) {
        		Map.Entry star = (Map.Entry)sit.next();
    	    	
    	    	String sName = (String) star.getKey();
    	    	String s_id ="";
    	    	if(!allStars.containsKey(sName)&&!sName.equals("NULL")) {
    	    		int sBirth = 0000;
	    			try {
	    				sBirth = ((Star)starsToInsert.get(sName)).getBirth();
	    				s2.setInt(3, sBirth);
	    			}
	    			catch(NullPointerException e){
	    				
	    				s2.setNull(3, java.sql.Types.INTEGER);
	    			}
	    			String sId = "nm"+(star_count+max_star);
	    			s2.setString(1, sId);
	    			s2.setString(2, sName);
	    			s2.addBatch();
	    			allStars.put(sName,s_id);
	    			star_count++;
    	    	}
        	}
        	Iterator SIMit = SIMToInsert.entrySet().iterator();
        	int SIM_count = 0;
        	while (SIMit.hasNext()) {
    	    	Map.Entry SIM = (Map.Entry)SIMit.next();
    	    	ArrayList<String> starList = (ArrayList<String>) SIM.getValue();
    	    	String mId = (String)SIM.getKey();
    	    	String mTitle = "NULL";
    	    	try {
    	    		mTitle = moviesToInsert.get(mId).getTitle();
    	    	}
    	    	catch(NullPointerException e)
    	    	{
    	    		System.out.println("Failed to add movie id: "+mId);
    	    		
    	    	}
    	    	if(allMovies.containsKey(mTitle)) // we can only link star with a existing movie
	    			mId = allMovies.get(mTitle);
    	    	else
    	    		mId = "NULL";
    	    	for(int i=0;i<starList.size();i++)
    	    	{
    	    		String sId ="";
    	    		String sName = starList.get(i);
    	    		
    	    		if(allStars.containsKey(sName)&&!sName.equals("NULL")&&!mId.equals("NULL")) {
    	    			
    	    			sId = allStars.get(sName);
    	    			
    	    			if(sId!=null && !sId.equals("")&&!allSIM.contains(sId+"-"+mId)) {
    	    				s4.setString(1, sId);
    	    				s4.setString(2, mId);
    	    				s4.addBatch();
    	    				allSIM.add(sId+"-"+mId);
    	    				SIM_count++;
    	    			}
    	    		}

    	    		
    	    	}
        	}
        	movieInserted = s1.executeBatch();
        	starInserted = s2.executeBatch();
        	genreInserted = s3.executeBatch();
        	SIMInserted = s4.executeBatch();
        	GIMInserted = s5.executeBatch();
        	RInserted = s6.executeBatch();
        	
        	connection.commit();

        	System.out.println("Successfully added to database ");
        		
        	}catch(Exception e) {
        		System.out.println("error in insertion");
        		e.printStackTrace();
        	}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void runparser() {
		
			parseXmlFile();
			parseMovie();
		    parseActors();
		    parseCasts();
			insertToDb();
		}
	public static void main(String [] args) {
		Parser res = new Parser();
		res.runparser();
	}
	
	
	
}