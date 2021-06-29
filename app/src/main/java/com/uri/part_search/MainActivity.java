package com.uri.part_search;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    //implements NavigationDrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemSelectedListener {

    //private NavigationDrawerFragment mNavigationDrawerFragment;
    private ListView matchesListView;
    private TextView infoBox;
    private EditText editCatNo;
    private EditText editDescription;
    ;
    private EditText editVoltage;
    private EditText editCurrent;
    private EditText editPower;
    private EditText editVa;
    private Spinner editType;
    private Spinner editPhase;
    private Spinner editDim;
    private EditText editVin;
    private Button button;
    private Button buttonOrder;
    private Button buttonCat;
    private TextView amper;
    private TextView watt;
    private TextView cR;
    private Spinner editCategory;
    private ProgressBar Progress;
    private ProgressBar Progress1;
    static public ArrayList<Match> matches = new ArrayList<Match>();
    static public JSONArray rows;
    private CharSequence mTitle;
    private Boolean isLoaded = false;
    String catNo, description, category, voltage, current, power, va, type, phase, dim;
    Float vin;
    //String url = "https://docs.google.com/spreadsheets/d/14pcekUttPmUraLYwq0dQumA0Ro7ecxc59f0Ye49LLZw/gviz/tq?tq=";
    String url = "https://docs.google.com/spreadsheets/d/1iua2WSlo_gJXI7HsxDLqM0qBjqoMTOdg/gviz/tq?tq=";
    String query[] = new String[11];
    String queryString = "";
    Activity activity;
    private String imei;
    private boolean boolA = true;
    private boolean boolW = true;
    private TelephonyManager tm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.matchesListView = (ListView) findViewById(R.id.matchesListView);
        this.infoBox = (TextView) findViewById(R.id.txtInfo);
        this.editCatNo = (EditText) findViewById(R.id.editTextCatNo);
        this.editDescription = (EditText) findViewById(R.id.editTextDescription);
        this.editCategory = (Spinner) findViewById(R.id.editTextCatalog);
        this.editVoltage = (EditText) findViewById(R.id.editTextVoltage);
        this.editCurrent = (EditText) findViewById(R.id.editTextCurrent);
        this.editPower = (EditText) findViewById(R.id.editTextPower);
        this.editVa = (EditText) findViewById(R.id.editTextVa);
        this.editType = (Spinner) findViewById(R.id.editTextType);
        this.editPhase = (Spinner) findViewById(R.id.editTextPhase);
        this.editDim = (Spinner) findViewById(R.id.editTextDim);
        this.Progress = (ProgressBar) findViewById(R.id.progressBar);
        this.Progress1 = (ProgressBar) findViewById(R.id.progressBar2);
        this.editVin = (EditText) findViewById(R.id.editTextVin);
        this.amper = (TextView) findViewById(R.id.textView8);
        this.watt = (TextView) findViewById(R.id.textView5);
        activity = this;
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    100);
            //infoBox.setText("הפעל הרשאה לטלפון");
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            //return;
        } else {
            imei = tm.getDeviceId();
            if (imei == null) {
                imei = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
            }
        }
        //mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        this.button = (Button) findViewById(R.id.buttonSearch);
        this.buttonOrder = (Button) findViewById(R.id.buttonOrder);
        this.buttonCat = (Button) findViewById(R.id.buttonCat);
        this.cR = (TextView) findViewById(R.id.textViewCR);
        final Button button2 = (Button) findViewById(R.id.buttonReset);
        reset(false);
        button.setEnabled(false);

        imei_check();

        editDim.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        editCategory.setOnItemSelectedListener(this);
        editType.setOnItemSelectedListener(this);
        editPhase.setOnItemSelectedListener(this);
        List<String> dimCategories = new ArrayList<String>();
        dimCategories.add("");
        dimCategories.add("Triac");
        dimCategories.add("1-10");
        dimCategories.add("Resistance");
        dimCategories.add("PWM");
        dimCategories.add("DALI");
        dimCategories.add("PUSH");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dimCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editDim.setAdapter(dataAdapter);
        List<String> catCategories = new ArrayList<String>();
        catCategories.add("");
        catCategories.add("ספק מודולרי");
        catCategories.add("ספק אטום");
        catCategories.add("אדפטורים");
        catCategories.add("ספק שולחני");
        catCategories.add("אל פסק");
        catCategories.add("דרייבר זרם");
        catCategories.add("דרייבר מתח");
        catCategories.add("ממיר DC/DC");
        catCategories.add("ממיר DC/AC");
        catCategories.add("מטענים");
        catCategories.add("ספק לפס דין");
        catCategories.add("ספק מודולרי דק");
        catCategories.add("אבטחה");
        catCategories.add("שנאים");
        catCategories.add("מצברים");
        catCategories.add("כללי");
        catCategories.add("ספק צר");
        catCategories.add("ספק פתוח");
        catCategories.add("לדים");
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCategory.setAdapter(catAdapter);
        List<String> typeCategories = new ArrayList<String>();
        typeCategories.add("");
        typeCategories.add("Interactiv");
        typeCategories.add("Online");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeCategories);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(typeAdapter);
        List<String> phaseCategories = new ArrayList<String>();
        phaseCategories.add("");
        phaseCategories.add("חד פאזי");
        phaseCategories.add("תלת פאזי");
        ArrayAdapter<String> phaseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phaseCategories);
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editPhase.setAdapter(phaseAdapter);

        amper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (boolA == true) {
                    amper.setText("זרם(mA)");
                    boolA = false;
                } else {
                    amper.setText("זרם(A)");
                    boolA = true;
                }
            }
        });
        watt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (boolW == true) {
                    watt.setText("הספק(kW)");
                    boolW = false;
                } else {
                    watt.setText("הספק(W)");
                    boolW = true;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                queryString = "";
                catNo = editCatNo.getText().toString();
                description = editDescription.getText().toString();
                //category = editCategory.getText().toString();
                voltage = editVoltage.getText().toString();
                DecimalFormat decimalFormat = new DecimalFormat("#0.#");
                current = boolA == true ? editCurrent.getText().toString() : decimalFormat.format(Float.valueOf(editCurrent.getText().toString()) / 1000);
                power = boolW == true ? editPower.getText().toString() : decimalFormat.format(Float.valueOf(editPower.getText().toString()) * 1000);
                va = editVa.getText().toString();
                if (editVin.getText().toString().length() != 0)
                    vin = Float.valueOf(editVin.getText().toString());
                try {
                    description = URLEncoder.encode(description, "utf-8");
                    category = URLEncoder.encode(category, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                float cFloat = editCurrent.getText().toString().equals("") ? 0 : Float.valueOf(current);
                query[0] = catNo.length() == 0 ? "" : "SELECT%20*%20WHERE%20LOWER(A)%20CONTAINS%20LOWER(%22" + catNo + "%22)";
                query[1] = description.length() == 0 ? "" : "SELECT%20*%20WHERE%20LOWER(B)%20CONTAINS%20LOWER(%22" + description + "%22)";
                query[2] = category.length() == 0 ? "" : "SELECT%20*%20WHERE%20C%20%3D%20%22" + category + "%22";
                query[3] = voltage.length() == 0 ? "" : "SELECT%20*%20WHERE%20(D%20%3D%20%22" + voltage + "%22%20OR%20I%20%3D%20%22" + voltage + "%22)";
                //query[4] = current.length() == 0 ? "" : "SELECT%20*%20WHERE%20E%20%3D%20%22" + current + "%22";
                query[4] = current.length() == 0 ? "" : "SELECT%20*%20WHERE%20(E%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20E%20%3E%3D%20(" + cFloat + "*0.9))" +
                        "%20OR%20(Z%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20Z%20%3E%3D%20(" + cFloat + "*0.9))" +
                        "%20OR%20(AA%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20AA%20%3E%3D%20(" + cFloat + "*0.9))" +
                        "%20OR%20(AB%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20AB%20%3E%3D%20(" + cFloat + "*0.9))" +
                        "%20OR%20(AC%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20AC%20%3E%3D%20(" + cFloat + "*0.9))" +
                        "%20OR%20(AD%20%3C%3D%20(" + cFloat + "*1.1)%20AND%20AD%20%3E%3D%20(" + cFloat + "*0.9))";
                query[5] = power.length() == 0 ? "" : "SELECT%20*%20WHERE%20F%20%3D%20%22" + power + "%22";
                query[6] = va.length() == 0 ? "" : "SELECT%20*%20WHERE%20V%20%3D%20%22" + va + "%22";
                query[7] = type.length() == 0 ? "" : "SELECT%20*%20WHERE%20W%20%3D%20%22" + type + "%22";
                query[8] = phase.length() == 0 ? "" : "SELECT%20*%20WHERE%20U%20%3D%20%22" + phase + "%22";
                query[9] = dim.length() == 0 ? "" : "SELECT%20*%20WHERE%20LOWER(Y)%20CONTAINS%20LOWER(%22" + dim + "%22)";
                query[10] = vin == null ? "" : "SELECT%20*%20WHERE%20K%3E%3D" + vin + "%20and%20J%3C%3D" + vin;
                for (int c = 0; c < 11; c++) {
                    if (query[c] != "") {
                        if (queryString == "")
                            queryString = queryString + query[c];
                        else
                            queryString = queryString + "%20AND%20" + query[c].substring(21);
                    }
                }
                String query = url + queryString;
                hideSoftKeyboard(activity);
                if (!query.isEmpty()) {
                    infoBox.setText("מחפש ...");
                    Progress.setVisibility(View.VISIBLE);
                    Progress1.setVisibility(View.VISIBLE);
                    matches.clear();
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            processJson(object);
                        }
                    }).execute(query);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(false);
            }
        });

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, Order.class);
                intent.putExtra("cli", true);
                startActivity(intent);
            }
        });

        buttonCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, Catalog.class);
                startActivity(intent);
            }
        });

        cR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                infoBox.setText(imei);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("imei", imei);
                clipboard.setPrimaryClip(clip);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Progress.setVisibility(View.GONE);
        Progress1.setVisibility(View.GONE);
        reset(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.editTextDim)
            dim = parent.getItemAtPosition(position).toString();

        if (spinner.getId() == R.id.editTextCatalog)
            category = parent.getItemAtPosition(position).toString();

        if (spinner.getId() == R.id.editTextType)
            type = parent.getItemAtPosition(position).toString();

        if (spinner.getId() == R.id.editTextPhase)
            phase = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    imei = tm.getDeviceId();
                    if (imei == null) {
                        imei = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
                    }
                    imei_check();
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                    this.finish();
                    System.exit(0);
                }
                return;
            }

            // other 'case' statements for other permssions
        }
    }

    public void onSectionAttached(int number) {

    }

    private void processJson(JSONObject object) {

        try {
            rows = object.getJSONArray("rows");
            if (rows.length() == 0) {
                infoBox.setText("אין תוצאות");
                Progress.setVisibility(View.GONE);
                Progress1.setVisibility(View.GONE);
                reset(true);
                return;
            }

                for (int r = 0; r < rows.length(); ++r) {
                    JSONObject row = rows.getJSONObject(r);
                    JSONArray columns = row.getJSONArray("c");
                    String catNo = (columns.get(0).toString() == "null") ? null : columns.getJSONObject(0).getString("v");
                    String description = (columns.get(1).toString() == "null") ? null : columns.getJSONObject(1).getString("v");
                    //String category = (columns.get(2).toString() == "null") ? null : columns.getJSONObject(2).getString("v");
                    Match m = new Match(catNo, description);
                    matches.add(m);
                }

                //String a = rows.getJSONObject(2).getJSONArray("c").getJSONObject(3).getString("v");;
                //final MatchesAdapter adapter = new MatchesAdapter(this, R.layout.match, matches);
                //matchesListView.setAdapter(adapter);
                //this.infoBox.setText("פריטים שנמצאו");
                Intent intent = new Intent(this, DisplayListActivity.class);
                //intent.putExtra("Matches", matches);
                intent.putExtra("order", "b");
                startActivity(intent);

            } catch(JSONException e){
                e.printStackTrace();
            }
    }

    private void imei_check() {
        String check = "https://docs.google.com/spreadsheets/d/1U89dmhwl3VLQD7wHCWF8jqXCArPFkKc6y9Y6a7X9vR4/gviz/tq?tq=" + "SELECT%20B%20WHERE%20B%20%3D%20%22" + imei + "%22";
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
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                checkJson(object);
            }
        }).execute(check);
    }

    private void checkJson(JSONObject object) {

        try {
            rows = object.getJSONArray("rows");
            if (rows.length() == 0) {
                infoBox.setText("לא ניתן להתחבר");
                button.setEnabled(false);
                buttonCat.setEnabled(false);
                buttonOrder.setEnabled(false);
                return;
            }
            else {
                infoBox.setText("");
                button.setEnabled(true);
                buttonCat.setEnabled(true);
                buttonOrder.setEnabled(true);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        }

    private void reset(boolean b) {
        if (!b)
        this.infoBox.setText("");
        this.editCatNo.setText("");
        this.editCategory.setSelection(0);
        this.editVoltage.setText("");
        this.editCurrent.setText("");
        this.editPower.setText("");
        this.editDescription.setText("");
        this.editVa.setText("");
        this.editType.setSelection(0);
        this.editPhase.setSelection(0);
        this.editDim.setSelection(0);
        this.editVin.setText("");
        dim = "";
        category = "";
        vin = null;
    }

    public static void hideSoftKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    /*public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }*/
    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /*public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }*/

}
