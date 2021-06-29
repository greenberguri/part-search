package com.uri.part_search;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Catalog extends Activity {
    private RecyclerView recyclerView;
    private Adapter adapter;
    public ArrayList matches;
    public ArrayList result;
    private TextView infoBox;
    private JSONArray rows;
    private Button button;
    private TextView editTextCatNo2;
    private Paint p = new Paint();
    private String pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogs);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        infoBox = (TextView) findViewById(R.id.txtInfo2);
        button = (Button) findViewById(R.id.buttonSearch2);
        editTextCatNo2 = (EditText) findViewById(R.id.editTextCatNo2);
        matches = new ArrayList();
        result = new ArrayList();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String check = "https://docs.google.com/spreadsheets/d/1w4q1m5-B1IyeWBWTPOGSWwnWz5zA9vbf/gviz/tq?tq=" + "SELECT%20*%20WHERE%20LOWER(A)%20CONTAINS%20LOWER(%22" + editTextCatNo2.getText() + "%22)";
                infoBox.setText("מתחבר לענן ...");
                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        try {
                            JSONArray error = object.getJSONArray("error");
                            if (error.length() == 0) {
                                infoBox.setText("בדוק את חיבורי הרשת");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        matches.clear();
                        result.clear();
                        checkJson(object);
                    }
                }).execute(check);
            }
        });
        enableSwipe();
    }

    private void checkJson(JSONObject object) {

        try {
            rows = object.getJSONArray("rows");
            if (rows.length() == 0) {
                infoBox.setText("אין תוצאות");
                return;
            }

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String cat = (columns.get(0).toString() == "null") ? null : columns.getJSONObject(0).getString("v");
                String res = (columns.get(1).toString() == "null") ? null : columns.getJSONObject(1).getString("v");
                matches.add(cat);
                result.add(res);
            }
            adapter = new Adapter(this, matches, result);
            recyclerView.setAdapter(adapter);

        } catch(JSONException e){
            e.printStackTrace();
        }
    }
    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    try {
                        JSONObject row = rows.getJSONObject(position);
                        JSONArray columns = row.getJSONArray("c");
                        pdf = columns.get(1).toString() == "null" ? "" : columns.getJSONObject(1).getString("v");
                        sendMail();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject row = rows.getJSONObject(position);
                        JSONArray columns = row.getJSONArray("c");
                        pdf = columns.get(1).toString() == "null" ? "" : columns.getJSONObject(1).getString("v");
                        sendMail();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void sendMail() {
        //ArrayList<Uri> attachments = pdf;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        //intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "קישור לדף הנתונים");
        intent.putExtra(Intent.EXTRA_TEXT, pdf);
        //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
