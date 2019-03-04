package edu.uci.ics.fabflixmobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class SingleMovie extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String ob = bundle.getString("message");
            try {
                JSONObject obj = new JSONObject(ob);
                ((TextView) findViewById(R.id.title)).setText(obj.getString("title"));
                ((TextView) findViewById(R.id.year)).setText(obj.getString("year"));
                ((TextView) findViewById(R.id.director)).setText("Director: "+obj.getString("director"));
                ((TextView) findViewById(R.id.star)).setText("Star: "+ obj.getString("star"));
                ((TextView) findViewById(R.id.genre)).setText("Genres: "+obj.getString("genre"));
            }catch(Exception e){

            }
        }
    }
}
