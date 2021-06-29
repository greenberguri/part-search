package com.uri.part_search;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayListActivity extends Activity {
    //private ArrayList matches = new ArrayList();
    private ListView matchesListView;
    private String order;
    private String result;
    private String client;
    private String transport;
    private String price;
    private ArrayList<String[]> body= new ArrayList<>();
    private String[][]cont;
    private String order_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        this.matchesListView = (ListView) findViewById(R.id.matchesListView);
        ArrayList Mmatches = MainActivity.matches;
        //Intent intent = getIntent();
        //matches = intent.getStringExtra(matches);
        Intent intent = getIntent();
        order = intent.getStringExtra("order");
        result = intent.getStringExtra("result");
        client = intent.getStringExtra("client");
        transport = intent.getStringExtra("transport");
        price = intent.getStringExtra("price");
        order_comment = intent.getStringExtra("order_comment");
        cont = (String[][]) getIntent().getSerializableExtra("body");

        final MatchesAdapter adapter = new MatchesAdapter(this, R.layout.match, Mmatches);
        matchesListView.setAdapter(adapter);

        matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (order.equals("a")){
                    Intent intent = new Intent(getApplicationContext(), Order.class);
                    intent.putExtra("id", Long.toString(id));
                    intent.putExtra("result", result);
                    intent.putExtra("client", client);
                    intent.putExtra("transport", transport);
                    intent.putExtra("price", price);
                    intent.putExtra("order_comment", order_comment);
                    intent.putExtra("body", cont);;
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ResultsItem.class);
                    intent.putExtra("id", Long.toString(id));
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

}

