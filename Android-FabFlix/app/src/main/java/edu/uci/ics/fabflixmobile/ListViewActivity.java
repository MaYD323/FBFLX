package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {
    int pageNum=1;
    int totalPage =100;
    String urlTitle = "";
    String urlYear = "";
    String urlDirector="";
    String urlStar = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        Bundle bundle = getIntent().getExtras();
        final ArrayList<Movie> MovieList = new ArrayList<>();
        if(bundle!=null) {
            try{

                JSONArray mArray = new JSONArray(bundle.getString("message"));
                for(int i =0;i<mArray.length();i++){
                    JSONObject m = mArray.getJSONObject(i);
                    String title = m.getString("movieTitle");
                    Integer year = m.getInt("movieYear");
                    String director = m.getString("movieDirector");
                    JSONArray gja = m.getJSONArray("genre");
                    pageNum = m.getInt("page");
                    totalPage = m.getInt("totalPage");
                    urlTitle = m.getString("urlTitle");
                    urlYear = m.getString("urlYear");
                    urlDirector = m.getString("urlDirector");
                    urlStar = m.getString("urlStar");
                    ArrayList<String> genreL = new ArrayList<>();
                    for(int z=0;z<gja.length();z++){
                        genreL.add(gja.getJSONObject(z).getString("name"));
                    }
                    JSONArray sja = m.getJSONArray("stars");
                    ArrayList<String> starL = new ArrayList<>();
                    for(int z=0;z<sja.length();z++){
                        starL.add(sja.getJSONObject(z).getString("name"));
                    }
                    Movie mov = new Movie(title,year,director,genreL,starL);
                    MovieList.add(mov);
                }
            }
            catch(Exception e){

            }

        } else { System.out.println("bundle is null-------------------");}

//        ArrayList<String> genre = new ArrayList<>();
//        genre.add("drama");
//        genre.add("Action");
//        MovieList.add(new Movie("Peter Anteater", 1965,"Eric",genre,genre));
//        MovieList.add(new Movie("John Doe", 1975,"Eric",genre,genre));

        PeopleListViewAdapter adapter = new PeopleListViewAdapter(MovieList, this);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = MovieList.get(position);
                JSONObject m = new JSONObject();
                try {
                    m.put("title", movie.getTitle());
                    m.put("year", movie.getYear());
                    m.put("director", movie.getDirector());
                    m.put("genre",movie.getGenre());
                    m.put("star",movie.getStar());
                    goToSingleMovies(m.toString());
                }catch(Exception e) {

                }

            }
        });
    }
    public void goToSingleMovies(String response) {
        Log.d("search.success", response);
        Intent goToIntent = new Intent(this, SingleMovie.class);
        goToIntent.putExtra("message", response);
        startActivity(goToIntent);
    }
    public void prevPage(View view){
        if(pageNum==1){
            return;
        }
        else{
            pageNum--;
            connectToSearch();


        }

    }
    public void nextPage(View view){
        if(pageNum==totalPage){
            return;
        }
        else{
            pageNum++;
            connectToSearch();

        }

    }
    public void connectToSearch() {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://3.17.207.121:8443/FabFlix/api/movies",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray obj= new JSONArray(response);
                            goToMovies(response);
                        }catch(Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data

                final Map<String, String> params = new HashMap<String, String>();
                params.put("title", urlTitle);
                params.put("year", urlYear);
                params.put("director", urlDirector);
                params.put("star", urlStar);
                params.put("sorting", "rating");
                params.put("npp", "5");
                params.put("page", Integer.toString(pageNum));
                params.put("genre", "");
                params.put("st", "");


                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }
    public void goToMovies(String response) {
        Log.d("search.success", response);
        Intent goToIntent = new Intent(this, ListViewActivity.class);
        goToIntent.putExtra("message", response);
        startActivity(goToIntent);
    }

}
