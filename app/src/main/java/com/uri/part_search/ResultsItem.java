package com.uri.part_search;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultsItem extends Activity {

    private TextView showCatNo, showDescription, showCategory, showVoltage, showCurrent, showPower, showDim, textCon;
    private TextView showIn, showPdf, showSize, showRange, showPrice, showBalance, showPhase, showQty, showConnector, showFan, textDim, showDate, showOrdered, showExpected;
    private TextView showSpower, showPpower, showType, showBat;
    private String a;
    private String id;
    private  int position;
    private ImageView IV;
    private String picture;
    private String pdf;
    private String imgURL;
    private Button mail;
    private ConstraintLayout upsLayout;
    private ConstraintLayout psLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_item);
        this.showCatNo = (TextView) findViewById(R.id.resultCatNo);
        this.showDescription = (TextView) findViewById(R.id.resultDescription);
        this.showCategory = (TextView) findViewById(R.id.resultCategory);
        this.showVoltage = (TextView) findViewById(R.id.resultVoltage);
        this.showCurrent = (TextView) findViewById(R.id.resultCurrent);
        this.showPower = (TextView) findViewById(R.id.resultPower);
        this.showSpower = (TextView) findViewById(R.id.Spower);
        this.showPpower = (TextView) findViewById(R.id.Ppower);
        this.showIn = (TextView) findViewById(R.id.resultIn);
        this.showPdf = (TextView) findViewById(R.id.textViewPdf);
        this.showSize = (TextView) findViewById(R.id.size);
        this.showRange = (TextView) findViewById(R.id.vRange);
        this.showQty = (TextView) findViewById(R.id.Qty);
        this.upsLayout = (ConstraintLayout) findViewById(R.id.constraintLayout4);
        this.psLayout = (ConstraintLayout) findViewById(R.id.constraintLayout3);
        this.showPhase = (TextView) findViewById(R.id.phase);
        this.showConnector = (TextView) findViewById(R.id.connector);
        this.showFan = (TextView) findViewById(R.id.fan);
        this.showDim = (TextView) findViewById(R.id.dimming);
        this.showType = (TextView) findViewById(R.id.UpsType);
        this.showBat = (TextView) findViewById(R.id.batNo);
        this.textDim = (TextView) findViewById(R.id.textView6);
        this.textCon = (TextView) findViewById(R.id.textView34);
        this.showPrice = (TextView) findViewById(R.id.price);
        this.showDate = (TextView) findViewById(R.id.date);
        this.showBalance = (TextView) findViewById(R.id.balance);
        this.showOrdered = (TextView) findViewById(R.id.ordered);
        this.showExpected = (TextView) findViewById(R.id.expected);
        this.IV = (ImageView) findViewById(R.id.iv);
        this.mail = (Button) findViewById(R.id.button);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        position = Integer.parseInt(id);
        upsLayout.setVisibility(View.GONE);
        showDim.setVisibility(View.GONE);
        textDim.setVisibility(View.GONE);
        resetFieleds();

        try {
            JSONObject row = MainActivity.rows.getJSONObject(position);
            JSONArray columns = row.getJSONArray("c");
            String A = columns.get(0).toString() == "null" ? "" : columns.getJSONObject(0).getString("v");
            String B = columns.get(1).toString() == "null" ? "" : columns.getJSONObject(1).getString("v");
            String C = columns.get(2).toString() == "null" ? "" : columns.getJSONObject(2).getString("v");
            String D = columns.get(3).toString() == "null" ? "" : columns.getJSONObject(3).getString("v") + "V";
            String E = columns.get(4).toString() == "null" ? "" : columns.getJSONObject(4).getString("v") + "A";
            String F = columns.get(5).toString() == "null" ? "" : columns.getJSONObject(5).getString("v") + "W";
            String I = columns.get(8).toString() == "null" ? "" : columns.getJSONObject(8).getString("v");
            String J = columns.get(9).toString() == "null" ? "" : columns.getJSONObject(9).getString("v");
            String K = columns.get(10).toString() == "null" ? "" : columns.getJSONObject(10).getString("v");
            String L = columns.get(11).toString() == "null" ? "" : columns.getJSONObject(11).getString("v");
            String M = columns.get(12).toString() == "null" ? "" : columns.getJSONObject(12).getString("v");
            String G = columns.get(6).toString() == "null" ? "" : columns.getJSONObject(6).getString("v") + "V";
            String H = columns.get(7).toString() == "null" ? "" : columns.getJSONObject(7).getString("v") + "V";
            String N = columns.get(13).toString() == "null" ? "" : columns.getJSONObject(13).getString("v");
            String S = columns.get(18).toString() == "null" ? "" : columns.getJSONObject(18).getString("v");
            String T = columns.get(19).toString() == "null" ? "" : columns.getJSONObject(19).getString("v");
            String U = columns.get(20).toString() == "null" ? "" : columns.getJSONObject(20).getString("v");
            String O = columns.get(14).toString() == "null" ? "" : columns.getJSONObject(14).getString("v");
            String P = columns.get(15).toString() == "null" ? "" : columns.getJSONObject(15).getString("v");
            String V = columns.get(21).toString() == "null" ? "" : columns.getJSONObject(21).getString("v");
            String W = columns.get(22).toString() == "null" ? "" : columns.getJSONObject(22).getString("v");
            String X = columns.get(23).toString() == "null" ? "" : columns.getJSONObject(23).getString("v");
            String Y = columns.get(24).toString() == "null" ? "" : columns.getJSONObject(24).getString("v");
            String Z = columns.get(25).toString() == "null" ? "" : columns.getJSONObject(25).getString("v");
            String AA = columns.get(26).toString() == "null" ? "" : columns.getJSONObject(26).getString("v");
            String AB = columns.get(27).toString() == "null" ? "" : columns.getJSONObject(27).getString("v");
            String AC = columns.get(28).toString() == "null" ? "" : columns.getJSONObject(28).getString("v");
            String AD = columns.get(29).toString() == "null" ? "" : columns.getJSONObject(29).getString("v");
            String AE = columns.get(30).toString() == "null" ? "" : columns.getJSONObject(30).getString("v");
            String AF = columns.get(31).toString() == "null" ? "" : columns.getJSONObject(31).getString("v");
            String AG = columns.get(32).toString() == "null" ? "" : columns.getJSONObject(32).getString("v");
            String AH = columns.get(33).toString() == "null" ? "" : columns.getJSONObject(33).getString("v");
            String AI = columns.get(34).toString() == "null" ? "" : columns.getJSONObject(34).getString("v");
            picture = columns.get(16).toString() == "null" ? "" : columns.getJSONObject(16).getString("v");
            pdf = columns.get(17).toString() == "null" ? "" : columns.getJSONObject(17).getString("v");
            if (C.equals("דרייבר זרם") || C.equals("דרייבר מתח")) {
                showDim.setVisibility(View.VISIBLE);
                textDim.setVisibility(View.VISIBLE);
                showConnector.setVisibility(View.GONE);
                textCon.setVisibility(View.GONE);
                //showFan.setVisibility(View.INVISIBLE);
            }
            if (C.equals("אל פסק")) {
                psLayout.setVisibility(View.GONE);
                upsLayout.setVisibility(View.VISIBLE);
            }
            showCatNo.setText(A);
            showDescription.setText(B);
            showCategory.setText(C);
            if (J != "" && K != "") {
                Float f1 = Float.parseFloat(J);
                Float f2 = Float.parseFloat(K);
                DecimalFormat decimal = new DecimalFormat("#0");
                J = decimal.format(f1).toString();
                K = decimal.format(f2).toString() + "Vdc";
            }
            D = I == "" ? D : D + " ," + I + "V";
            L = L == "" ? J + "-" + K : L;
            E = Z == "" ? E : E + " ," + Z + "A";
            E = AA == "" ? E : E + " ," + AA + "A";
            E = AB == "" ? E : E + " ," + AB + "A";
            E = AC == "" ? E : E + " ," + AC + "A";
            E = AD == "" ? E : E + " ," + AD + "A";
            //if (I == "")
              //  showVoltage.setText(D + "V");
            //else
              //  showVoltage.setText(D + "V" + " ," + I + "V");
            showVoltage.setText(D);
            showCurrent.setText(E);
            showPower.setText(F);
            showIn.setText(L);
            showSize.setText(M);
            showRange.setText(G + "-" + H);
            showQty.setText(N);
            showConnector.setText(S);
            showPhase.setText(U);
            showFan.setText(T);
            showDim.setText(Y);
            showPpower.setText(F);
            showSpower.setText(V);
            showType.setText(W);
            showBat.setText(AE);
            showPrice.setText(P);
            showBalance.setText(AF);
            showOrdered.setText(AH);
            showExpected.setText(AI);
            showDate.setText("עודכן ב: " + AG);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if (picture != "") {
            //imgURL = "https://drive.google.com/uc?export=download&id=" + picture;
            imgURL = picture;
            new DownLoadImageTask(IV).execute(imgURL);
        }
        //showPdf.setText(pdf);
        if (pdf == "") {
            showPdf.setText("דף נתונים לא זמין");
            showPdf.setEnabled(false);
            mail.setEnabled(false);
        }
        else {
            showPdf.setClickable(true);
            showPdf.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='" + pdf + "'> קישור לדף הנתונים </a>";
            showPdf.setText(Html.fromHtml(text));

            mail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendMail();
                }
            });
        }
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
    private void resetFieleds() {
        this.showCatNo.setText("");
        this.showDescription.setText("");
        this.showCategory.setText("");
        this.showVoltage.setText("");
        this.showCurrent.setText("");
        this.showPower.setText("");
        this.showIn.setText("");
        this.showPdf.setText("");
        this.showSize.setText("");
        this.showPhase.setText("");
        this.showConnector.setText("");
        this.showQty.setText("");
        this.showFan.setText("");
        this.showPrice.setText("");
        this.showBalance.setText("");
        this.showOrdered.setText("");
        this.showExpected.setText("");
    }
}
