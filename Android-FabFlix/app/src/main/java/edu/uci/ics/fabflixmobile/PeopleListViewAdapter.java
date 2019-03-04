package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PeopleListViewAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movieList;

    public PeopleListViewAdapter(ArrayList<Movie> movieList, Context context) {
        super(context, R.layout.layout_listview_row, movieList);
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_listview_row, parent, false);

        Movie movie = movieList.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        TextView subtitleView = (TextView)view.findViewById(R.id.subtitle);
        TextView dir = (TextView)view.findViewById(R.id.director);
        TextView gen = (TextView)view.findViewById(R.id.Genres);
        TextView st = (TextView)view.findViewById(R.id.star);

        titleView.setText(movie.getTitle());
        subtitleView.setText(movie.getYear().toString());
        dir.setText(movie.getDirector().toString());
        gen.setText(movie.getGenre().toString());
        st.setText(movie.getStar().toString());

        return view;
    }
}
