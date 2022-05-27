package com.example.timetablemai;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Spinner spinner;
    TextView textViewGroup;
    SharedPreferences sharedPreferences;
    static ArrayList<String> groupsName = new ArrayList<>();
    static HashMap<String, Object> myMapCurrentGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsToolbarNavigationViewDrawerLayout();
        textViewGroup = findViewById(R.id.textViewGroup);
        spinner = findViewById(R.id.spinner);
        JsonsParser jsonParser = new JsonsParser();

        jsonParser.execute();


       // }

        sharedPreferences = getSharedPreferences("mPreferences",MODE_PRIVATE);
        String myGroup = "";
        myGroup = sharedPreferences.getString("groupName", "не выбрана");
        if (!myGroup.equals("")){
            textViewGroup.setText("Группа: " + myGroup);
            SettingsActivity.groupName = myGroup;
        }
        if(date.size()!= 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, date);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

    }

    private void settingsToolbarNavigationViewDrawerLayout() {
        toolbar = findViewById(R.id.toolbarMain);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.site:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mai.ru/")));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.info:
                startActivity(new Intent(this, info_activity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;


        }
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case (R.id.settings):
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public void onMyButtonWeek(View view) {


        startActivity(new Intent(this, ActivityWeekTimetable.class));

    }

    public class JsonToStr {
        public String get(String urlString) throws Exception {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();

            StringBuilder result = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }

    }
    public class JsonsParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String sJson;
                JsonToStr jsonToStr = new JsonToStr();
                sJson = jsonToStr.get("https://public.mai.ru/schedule/data/groups.json");
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();

                ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String, String>>();

                arrayList = gson.fromJson(sJson, type);

                groupsName.clear();
                for (int i = 0; i < arrayList.size(); i++) {
                    for (HashMap.Entry<String, String> entry : arrayList.get(i).entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key.equals("name")) {
                            groupsName.add(value);
                        }
                    }
                }
            } catch (Exception e) {
                Log.v("exception", e.toString());

            }
            JsonToStr jsonToStr = new JsonToStr();
            if(SettingsActivity.groupName != null) {
                try {
                    jsonObjTimeTable = jsonToStr.get("https://public.mai.ru/schedule/data/"+md5(SettingsActivity.groupName)+".json");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonObjTimeTable);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    printJsonObject(jsonObject, 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (discName2 != null)
                for (int counter = 0; counter <discName2.size(); counter++) {
                    Log.i("date1111",(discName2.get(counter)));
                }
            return null;
        }
        private HashMap<String, Object> getHashMapFromJson(String json) throws JSONException {
            myMapCurrentGroup = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(json);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext();) {
                String key = it.next();
                myMapCurrentGroup.put(key, jsonObject.get(key));
            }
            return myMapCurrentGroup;
        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, date);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                        TextView mt = (TextView) selectedItemView;
                        dateSelected = (String) mt.getText();
                        discName2.clear();
                        time_end.clear();
                        time_start.clear();
                        lector.clear();
                        room.clear();
                        type.clear();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonObjTimeTable);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            printJsonObject(jsonObject, 0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (discName2 != null)
                            for (int counter = 0; counter <discName2.size(); counter++) {
                                Log.i("disc1111",(discName2.get(counter)));
                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
        }
    }
    static String jsonObjTimeTable;
    static ArrayList<String> discName2 = new ArrayList<>();
    static ArrayList<String> date = new ArrayList<>();
    static ArrayList<String> time_start = new ArrayList<>();
    static ArrayList<String> time_end = new ArrayList<>();
    static ArrayList<String> lector = new ArrayList<>();
    static ArrayList<String> type = new ArrayList<>();
    static ArrayList<String> room = new ArrayList<>();
    static String dateSelected ="";
    public void printJsonObject(JSONObject jsonObj, int lvl) throws JSONException {

        for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
            int tmp;
            String keyStr = it.next();
            Object keyvalue = jsonObj.get(keyStr);
            //Print key and value
            Log.i("JsonTest3","key: "+ keyStr + " value: " + keyvalue);
            //for nested objects iteration if required
            if (dateSelected.equals("")) {

                if ((lvl == 0) & (!keyStr.equals("group")))
                    date.add(keyStr);
            }
            else if (keyStr.equals(dateSelected)) {
                printJsonObject2((JSONObject) keyvalue, tmp= lvl+1);
            }

            if (keyvalue instanceof JSONObject) {
                printJsonObject((JSONObject) keyvalue, tmp = lvl + 1);
            }
        }
    }
    public void printJsonObject2(JSONObject jsonObj, int lvl) throws JSONException {

        for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
            String keyStr = it.next();
            Object keyvalue = jsonObj.get(keyStr);
            //for nested objects iteration if required
                if (lvl == 3)
                    discName2.add(keyStr);
                else if (lvl == 4) {
                    if (keyStr.equals("time_start"))
                        time_start.add((String) keyvalue);
                    else if (keyStr.equals("time_end"))
                        time_end.add((String) keyvalue);
                    else if (keyStr.equals("lector"))
                        lector.add((String) ((JSONObject) keyvalue).get((((JSONObject) keyvalue).keys()).next()));
                    else if (keyStr.equals("type"))
                        type.add((String) ((((JSONObject) keyvalue).keys()).next()));
                    else if (keyStr.equals("room"))
                        room.add((String) ((JSONObject) keyvalue).get((((JSONObject) keyvalue).keys()).next()));
                }
            int tmp;
            if (keyvalue instanceof JSONObject) {
                printJsonObject2((JSONObject) keyvalue, tmp = lvl + 1);
            }
        }
    }


    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}