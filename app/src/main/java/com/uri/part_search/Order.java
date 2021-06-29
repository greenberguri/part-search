package com.uri.part_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListPopupWindow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Order extends Activity implements View.OnTouchListener,
        AdapterView.OnItemClickListener {

    private EditText editTextName;
    private EditText editTextCatNo;
    private EditText editTextAmount;
    private EditText editTextTransport;
    private EditText editTextComments;
    private EditText editTextPrice;
    private TextView textViewPrice;
    private TextView textViewInStock;
    private Button buttonSearch;
    private Button buttonOrder;
    private Button buttonAdd;
    private Button buttonOrderCom;
    private Button buttonCancel;
    private TextView textViewList;
    private String queryString;
    private String catNo;
    private TextView infoBox;
    private ProgressBar Progress;
    private ProgressBar Progress1;
    String url = "https://docs.google.com/spreadsheets/d/1iua2WSlo_gJXI7HsxDLqM0qBjqoMTOdg/gviz/tq?tq=";
    static public JSONArray rows;
    private String id = "";
    private  int position;
    private String showCatNo;
    private String result = "";
    private String agent = "";
    private String price = "";
    private String[] list = new String[4];
    private ArrayList<String[]> body= new ArrayList<>();
    private String subject = "הזמנה עבור ";
    private String client;
    private String transport;
    private String[][] cont;
    private SharedPreferences app_preferences;
    private Boolean cli = false;
    private ListPopupWindow lpw;
    private String[] dropList;
    private String order_comment = "";
    private String originalPrice;
    private String inStock;
    private String username = "";
    private String password = "";
    private static final String emailid = "nrg@avivenergy.co.il";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        this.editTextName = (EditText) findViewById(R.id.editTextName);
        this.editTextCatNo = (EditText) findViewById(R.id.editTextCatNo);
        this.editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        this.editTextTransport = (EditText) findViewById(R.id.editTextTransport);
        this.editTextComments = (EditText) findViewById(R.id.editTextComments);
        this.editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        this.textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        this.textViewInStock = (TextView) findViewById(R.id.textViewInStock);
        this.buttonSearch = (Button) findViewById((R.id.buttonSearch));
        this.buttonOrder = (Button) findViewById((R.id.buttonOrder));
        this.buttonAdd = (Button) findViewById((R.id.buttonAdd));
        this.buttonOrderCom = (Button) findViewById((R.id.buttonOrderCom));
        this.buttonCancel = (Button) findViewById((R.id.buttonCancel));
        this.textViewList = (TextView) findViewById(R.id.textViewList);
        this.infoBox = (TextView) findViewById(R.id.txtInfo);
        this.Progress = (ProgressBar) findViewById(R.id.progressBar);
        this.Progress1 = (ProgressBar) findViewById(R.id.progressBar2);
        editTextName.requestFocus();

        editTextTransport.setOnTouchListener(this);
        dropList = new String[] { "דואר", "לקוח", "גיל", "רדה", "אודי", "אבי", "מיכה", "אנחנו"};
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dropList));
        lpw.setAnchorView(editTextTransport);
        lpw.setModal(true);
        lpw.setOnItemClickListener(this);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        agent = app_preferences.getString("agent" , "");
        username = app_preferences.getString("username" , "");
        password = app_preferences.getString("password" , "");
        Intent intent = getIntent();
        cli = intent.getBooleanExtra("cli",false);
        if(agent == "" || username == "" || password == "")
            agent_input();
        else if (cli) {
            Toast.makeText(Order.this, "שם הסוכן:" + agent, Toast.LENGTH_SHORT).show();
        }
        getIntent().removeExtra("cli");
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = editTextName.getText().toString();
                transport = editTextTransport.getText().toString();
                queryString = "";
                catNo = editTextCatNo.getText().toString();
                queryString = catNo.length() == 0 ? "" : "SELECT%20*%20WHERE%20LOWER(A)%20CONTAINS%20LOWER(%22" + catNo + "%22)";

                String query = url + queryString;
                //MainActivity.hideSoftKeyboard(Order.this);
                if (!query.isEmpty()) {
                    infoBox.setText("מחפש ...");
                    Progress.setVisibility(View.VISIBLE);
                    Progress1.setVisibility(View.VISIBLE);
                    MainActivity.matches.clear();
                    new DownloadWebpageTask(new AsyncResult() {
                        @Override
                        public void onResult(JSONObject object) {
                            try {
                                JSONArray error = object.getJSONArray("error");
                                if (error.length() == 0) {
                                    infoBox.setText("בדוק את חיבורי הרשת");
                                    Progress.setVisibility(View.GONE);
                                    Progress1.setVisibility(View.GONE);
                                    return;
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            processJson(object);
                        }
                    }).execute(query);
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editTextAmount.getText().toString().equals("") && !editTextCatNo.getText().toString().equals("") && !editTextPrice.getText().toString().equals("")) {
                    infoBox.setText("");
                    list[0] = editTextCatNo.getText().toString();
                    list[1] = editTextAmount.getText().toString();
                    String q = editTextPrice.getText().toString();
                    if (editTextPrice.getText().toString().equals("."))
                        list[2] = "מערכת";
                    else
                        list[2] = editTextPrice.getText().toString();
                    list[3] = editTextComments.getText().toString();
                    body.add(list);
                    result = result + editTextCatNo.getText() + " ..... ₪" + editTextPrice.getText() + " ..... " + editTextAmount.getText() + " pcs." + "        " + editTextComments.getText() + "\n";
                    textViewList.setText(result);
                    reset(false);
                }
                else {
                    if (editTextAmount.getText().toString().equals("")) {
                        infoBox.setText("הכנס כמות");
                        Toast.makeText(Order.this, "הכנס כמות", Toast.LENGTH_SHORT).show();
                    }
                    if (editTextCatNo.getText().toString().equals("")) {
                        infoBox.setText("הכנס מקט");
                        Toast.makeText(Order.this, "הכנס מקט", Toast.LENGTH_SHORT).show();
                    }
                    if (editTextPrice.getText().toString().equals("")) {
                        infoBox.setText("הכנס מחיר");
                        Toast.makeText(Order.this, "הכנס מחיר", Toast.LENGTH_SHORT).show();
                    }
                }
                editTextCatNo.requestFocus();
            }
        });

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (order_comment == null)
                    order_comment = "";
                client = editTextName.getText().toString();
                if (client != "") {
                    transport = editTextTransport.getText().toString();
                    sendMail(emailid, subject + editTextName.getText(), htmlMessageBody(body).toString());
                    reset(true);
                    textViewList.setText(result);
                    cont = null;
                }
                else {
                    infoBox.setText("הכנס שם לקוח");
                    Toast.makeText(Order.this, "הכנס שם לקוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonOrderCom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comment_input();
            }
            });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(true);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Progress.setVisibility(View.GONE);
        Progress1.setVisibility(View.GONE);
        reset(false);
        //textViewList.setText(result);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (result == "")
        result = intent.getStringExtra("result");
        client = intent.getStringExtra("client");
        transport = intent.getStringExtra("transport");
        price = intent.getStringExtra("price");
        order_comment = intent.getStringExtra("order_comment");
        getIntent().removeExtra("id");
        getIntent().removeExtra("result");
        getIntent().removeExtra("client");
        getIntent().removeExtra("transport");
        getIntent().removeExtra("price");
        cont = (String[][]) getIntent().getSerializableExtra("body");
        getIntent().removeExtra("body");
        result =  result == null ? "" : result;
        textViewList.setText(result);
        if (id != null) {
            position = Integer.parseInt(id);
            try {
                JSONObject row = rows.getJSONObject(position);
                JSONArray columns = row.getJSONArray("c");
                showCatNo = columns.get(0).toString() == "null" ? "" : columns.getJSONObject(0).getString("v");
                originalPrice = columns.get(15).toString() == "null" ? "" : columns.getJSONObject(15).getString("v");
                inStock = columns.get(31).toString() == "null" ? "" : columns.getJSONObject(31).getString("v");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            body = new ArrayList<>(Arrays.asList(cont));
            editTextName.setText(client);
            editTextTransport.setText(transport);
            editTextCatNo.setText(showCatNo);
            textViewPrice.setText(originalPrice);
            textViewInStock.setText(inStock);
            //textViewList.setText(showCatNo + "  " + editTextAmount.getText());
            if (!editTextCatNo.getText().toString().equals("")) {
                editTextAmount.requestFocus();
            }
        }
    }

    private void processJson(JSONObject object) {

        try {
            rows = object.getJSONArray("rows");
            if (rows.length() == 0) {
                infoBox.setText("אין תוצאות");
                Progress.setVisibility(View.GONE);
                Progress1.setVisibility(View.GONE);
                reset(false);
                return;
            }

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String catNo = (columns.get(0).toString() == "null") ? null : columns.getJSONObject(0).getString("v");
                String description = (columns.get(1).toString() == "null") ? null : columns.getJSONObject(1).getString("v");
                Match m = new Match(catNo, description);
                MainActivity.matches.add(m);
            }

            Intent intent = new Intent(this, DisplayListActivity.class);
            intent.putExtra("order", "a");
            intent.putExtra("result", result);
            intent.putExtra("client", client);
            intent.putExtra("transport", transport);
            intent.putExtra("price", price);
            intent.putExtra("order_comment", order_comment);
            cont = body.toArray(new String[0][0]);
            intent.putExtra("body", cont);
            startActivity(intent);
            finish();

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void reset(boolean b) {
        if (b) {
            body.clear();
            result = "";
            order_comment = "";
            this.editTextName.setText("");
            this.infoBox.setText("");
            this.editTextCatNo.setText("");
            this.editTextAmount.setText("");
            this.editTextTransport.setText("");
            this.editTextComments.setText("");
            this.textViewList.setText("");
            this.textViewInStock.setText("");
            this.textViewPrice.setText("");
            this.editTextPrice.setText("");
        }
        else {
            this.infoBox.setText("");
            this.editTextCatNo.setText("");
            this.editTextAmount.setText("");
            this.editTextComments.setText("");
            this.textViewInStock.setText("");
            this.textViewPrice.setText("");
            this.editTextPrice.setText("");
        }
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws

            MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, agent));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setContent(htmlMessageBody(body).toString(), "text/html; charset=utf-8");
        //message.setText(messageBody);
        return message;
    }


    public class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Order.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(javax.mail.Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sendMailSelector(String emailid, String subject, String messageBody) {
        Intent i = new Intent(Intent.ACTION_SEND);
        //Intent i = new Intent(Intent.ACTION_SENDTO); //for mobile outlook
        //i.setData(Uri.parse("mailto:youremail@gmail.com" + "?subject=" + Uri.encode("some subject text here") + "&body=" + Uri.encode("some text here")));
        //i.setType("message/rfc822");
        //i.setType("text/plain");
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailid});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , messageBody);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            //Toast.makeText(Order.this, "הזמנה נשלחה", Toast.LENGTH_SHORT).show();
            reset(true);
            textViewList.setText(result);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Order.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void agent_input() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Order.this);
        alertDialog.setTitle("פרטי הסוכן");
        final View customLayout = getLayoutInflater().inflate(R.layout.custom, null);
        alertDialog.setView(customLayout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText PersonName = customLayout.findViewById(R.id.editTextTextPersonName);
                EditText PersonEmailAddress = customLayout.findViewById(R.id.editTextTextEmailAddress);
                EditText PersonPassword = customLayout.findViewById(R.id.editTextTextPassword);
                agent = PersonName.getText().toString();
                username = PersonEmailAddress.getText().toString();
                password = PersonPassword.getText().toString();
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("agent", agent);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                infoBox.setText("");
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                infoBox.setText("לא ניתן לשלוח ללא פרטי סוכן");
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void comment_input() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הערה כללית להזמנה");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order_comment = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private StringBuilder htmlMessageBody(ArrayList body) {
        StringBuilder sb = new StringBuilder();
        //create html & table
        sb.append("<html>\n" +
                "<body style=\"margin:0;padding:0;background-color:#eee\">\n" +
                "<center>\n" +
                "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"bodyTable\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding:0;background-color:#eee;height:100%;margin:0;width:100%\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "            <td align=\"center\" id=\"bodyCell\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding-top:50px;padding-left:20px;padding-bottom:20px;padding-right:20px;border-top:0;height:100%;margin:0;width:100%\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"templateContainer\" width=\"600\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;border:0 none #aaa;background-color:#fff;border-radius:0\">\n" +
                "                    <tbody>\n" +
                "                    <tr>\n" +
                "                        <td class=\"templateContainerInner\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding:0\">\n" +
                "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                <tr>\n" +
                "                                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"templateRow\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                            <tbody>\n" +
                "                                            <tr>\n" +
                "                                                <td class=\"rowContainer kmFloatLeft\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmImageBlock\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                        <tbody class=\"kmImageBlockOuter\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td class=\"kmImageBlockInner\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding:9px;background-color:#CE0909;\" valign=\"top\">\n" +
                "                                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmImageContentContainer\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                                    <tbody>\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td class=\"kmImageContent\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding:0;padding-top:0px;padding-bottom:0;padding-left:9px;padding-right:9px;text-align: center;\">\n" +
                "                                                                            \n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                    </tbody>\n" +
                "                                                                </table>\n" +
                "                                                            </td>\n" +
                "                                                        </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmTextBlock\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                        <tbody class=\"kmTextBlockOuter\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td class=\"kmTextBlockInner\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;\">\n" +
                "                                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmTextContentContainer\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                                    <tbody>\n" +
                "                                                                    <tr>\n" +
                "                                                                        <td class=\"kmTextContent\" valign=\"top\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#009999;font-family:\"Helvetica Neue\", Arial;font-size:20px;line-height:130%;text-align:right;padding-top:50px;padding-bottom:9px;padding-left:18px;padding-right:18px;'>\n" +
                "                                                                            <p>סוכן: " + agent + "</p>\n" +
                "                                                                            <td class=\"kmTextContent\" valign=\"top\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#ff00ff;font-family:\"Helvetica Neue\", Arial;font-size:16px;line-height:130%;text-align:right;padding-top:9px;padding-bottom:9px;padding-left:18px;padding-right:18px;'>\n" +
                "                                                                            <h1><u>הזמנה</u></h1>\n" +
                "                                                                        </td>\n" +
                "                                                                          <td class=\"kmTextContent\" valign=\"top\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#ff3300;font-family:\"Helvetica Neue\", Arial;font-size:20px;line-height:130%;text-align:right;padding-top:50px;padding-bottom:9px;padding-left:18px;padding-right:18px;'>\n" +
                "                                                                            <p>לקוח: " + client + "</p>" +
                "                                                                        \n" +
                "                                                                    </tr>\n" +
                "                                                                    </tbody>\n" +
                "                                                                </table>\n" +
                "                                                            </td>\n" +
                "                                                        </tr>\n" +
                "                                                        </tbody>\n" +
                "                                                    </table>\n" +
                "                                                     <table dir=\"rtl\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmTableBlock kmTableMobile\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0\">\n" +
                "                                                        <tbody class=\"kmTableBlockOuter\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td class=\"kmTableBlockInner\" valign=\"top\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;padding-top:9px;padding-bottom:9px;padding-left:0px;padding-right:0px;\">\n" +
                "                                                                <table align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"kmTable\" width=\"100%\" style=\"border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;\">\n" +
                "                                                                    <thead>\n" +
                "                                                                    <tr>\n" +
                "                                                                    \n" +
                "                                                                    <th valign=\"top\" class=\"kmTextContent tdwidth\" style='color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;text-align:center;padding-top:4px;font-weight:bold;padding-right:0px;padding-left:0px;padding-bottom:4px;'>מקט</th>\n" +
                "                                                                     <th valign=\"top\" class=\"kmTextContent tdwidth\" style='color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;text-align:center;padding-top:4px;font-weight:bold;padding-right:0px;padding-left:0px;padding-bottom:4px;'>כמות</th>\n" +
                "                                                                     <th valign=\"top\" class=\"kmTextContent tdwidth\" style='color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;text-align:center;padding-top:4px;font-weight:bold;padding-right:0px;padding-left:0px;padding-bottom:4px;'>מחיר</th>\n" +
                "                                                                      <th valign=\"top\" class=\"kmTextContent\" style='color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;text-align:center;padding-top:4px;font-weight:bold;padding-right:0px;padding-left:0px;padding-bottom:4px;'>הערות</th>\n" +
                "                                                                        \n" +
                "                                                                    </tr>\n" +
                "                                                                    </thead>\n" +
                "                                                                    <tbody>\n" +
                "                                                                    <tr class=\"kmTableRow\">");

        for (int i = 0; i < body.size() ; i++) {
            sb.append("<td valign=\"top\" class=\"kmTextContent\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;border-right:none;text-align:center;;border-top-style:solid;padding-bottom:4px;padding-right:0px;padding-left:0px;padding-top:4px;border-top-color:#d9d9d9;border-top-width:1px;'>" + ((String[]) body.get(i))[0] + "</td>\n" +
                    "                                                                        \n" +
                    "                                                                      <td valign=\"top\" class=\"kmTextContent tdwidth\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;text-align:center;;border-top-style:solid;padding-bottom:4px;padding-right:0px;padding-left:0px;padding-top:4px;border-top-color:#d9d9d9;border-top-width:1px;'>" + ((String[]) body.get(i))[1] + "</td>\n" +
                    "                                                                        \n" +
                    "                                                                       <td valign=\"top\" class=\"kmTextContent\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;border-right:none;text-align:center;;border-top-style:solid;padding-bottom:4px;padding-right:0px;padding-left:0px;padding-top:4px;border-top-color:#d9d9d9;border-top-width:1px;'>" + ((String[]) body.get(i))[2] + "</td>\n" +
                    "                                                                         \n" +
                    "                                                                        <td valign=\"top\" class=\"kmTextContent\" style='border-collapse:collapse;mso-table-lspace:0;mso-table-rspace:0;color:#222;font-family:\"Helvetica Neue\", Arial;font-size:14px;line-height:130%;text-align:left;border-right:none;text-align:center;;border-top-style:solid;padding-bottom:4px;padding-right:0px;padding-left:0px;padding-top:4px;border-top-color:#d9d9d9;border-top-width:1px;'>" + ((String[]) body.get(i))[3] + "</td>\n" +
                    "                                                                       ");


            sb.append("</tr>");
        }
        sb.append("</tbody>\n" +
                "</table><p>&nbsp;</p> \n" +
                "<p>סוג משלוח: " + transport + "</p>\n" +
                "<p>הערה: " + order_comment + "</p>\n" +
                "\n" +
                "\n" +
                "                                                       \n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            </tbody>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "</center>\n" +
                "</body>\n" +
                "</html>\n");
        return sb;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = dropList[position];
        editTextTransport.setText(item);
        lpw.dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                lpw.show();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}