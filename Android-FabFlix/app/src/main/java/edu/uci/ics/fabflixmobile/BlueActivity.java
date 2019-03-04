package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BlueActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
        }
    }
    public void goToMovies(String response) {
        Log.d("search.success", response);
        Intent goToIntent = new Intent(this, ListViewActivity.class);
        goToIntent.putExtra("message", response);
        startActivity(goToIntent);
    }



    public void connectToSearch(View view) {

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
                String t = ((EditText) findViewById(R.id.title)).getText().toString();
                String y = ((EditText) findViewById(R.id.year)).getText().toString();
                String d = ((EditText) findViewById(R.id.director)).getText().toString();
                String s = ((EditText) findViewById(R.id.star)).getText().toString();
                params.put("title", t);
                params.put("year", y);
                params.put("director", d);
                params.put("star", s);
                params.put("sorting", "rating");
                params.put("npp", "5");
                params.put("page", "1");
                params.put("genre", "");
                params.put("st", "");


                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }
}
