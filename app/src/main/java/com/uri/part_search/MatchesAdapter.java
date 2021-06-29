package com.uri.part_search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchesAdapter extends ArrayAdapter<Match> {

    Context context;
    private ArrayList<Match> matches;

    public MatchesAdapter(Context context, int textViewResourceId, ArrayList<Match> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.matches = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.match, null);
        }
        Match match = matches.get(position);
        if (match != null) {
            TextView catNo = (TextView) v.findViewById(R.id.catNo);
            TextView description = (TextView) v.findViewById(R.id.description);
            //TextView day = (TextView) v.findViewById(R.id.day);
            //TextView homeTeam = (TextView) v.findViewById(R.id.homeTeam);
            //TextView homeScore = (TextView) v.findViewById(R.id.homeScore);
            //TextView awayTeam = (TextView) v.findViewById(R.id.awayTeam);
            //TextView awayScore = (TextView) v.findViewById(R.id.awayScore);
            catNo.setText(String.valueOf(match.getCatNo()));
            description.setText(String.valueOf(match.getDescription()));
            //day.setText(String.valueOf(match.getDay()));
            //homeTeam.setText(String.valueOf(match.getHomeTeam()));
            //homeScore.setText(String.valueOf(match.getHomeScore()));
            //awayTeam.setText(String.valueOf(match.getAwayTeam()));
            //awayScore.setText(String.valueOf(match.getAwayScore()));
        }
        return v;
    }
}
