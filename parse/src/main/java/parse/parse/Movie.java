package parse.parse;
import java.util.*;
public class Movie{
	
	public String id;
	public String title;
	public int year;
	public String director;
	public ArrayList<String> genre;

	
	public Movie(){
	}
	
	public Movie(String id, String title, int year, String director, ArrayList<String> genre){
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.genre = genre;

	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String nid) {
		this.id = nid;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String ntitle) {
		this.title = ntitle;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int nyear) {
		this.year = nyear;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String ndirector) {
		this.director = ndirector;
	}
	
	public ArrayList<String> getGenre() {
		return this.genre;
	}

	public void setGenre(ArrayList<String> ngenre) {
		this.genre = ngenre;
	}
	
	public String toString() {
        String s = "Movie Id: "+this.id + " Title: "+this.title+" director: "+this.director+" Year: "+ this.year+" Genres: ";
        for(String g:this.genre){
            s=s+g+", ";
        }
        return s;
	}
	
	
	


}
