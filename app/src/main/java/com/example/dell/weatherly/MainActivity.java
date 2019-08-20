package com.example.dell.weatherly;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField, windspeed;
    Typeface weatherFont;
    private ProgressDialog pDialog;
    private String url;
    final String[] z = {""};

    public static final String PREFS = "examplePrefs";
    public static final String PREFS1 = "examplePrefs1";
    public static final String PREFS2 = "examplePrefs2";
    public static final String PREFS3 = "examplePrefs3";
    public static final String PREFS4 = "examplePrefs4";

    private double currentLatitude,currentLongitude;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //getLocation();

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        windspeed = (TextView)findViewById(R.id.wind_speed);
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        SharedPreferences ex1 = getSharedPreferences(PREFS, 0);
        String c = ex1.getString("message", "not found");
        if(c.equals("not found")) {
            SharedPreferences.Editor editor = ex1.edit();
            editor.putString("message", "Allahabad, Uttar Pradesh, India 25.4358 81.8463");
            editor.commit();
        }
        SharedPreferences ex2 = getSharedPreferences(PREFS, 0);
        String c1=ex2.getString("message", "not found");

        int pos_long = c1.lastIndexOf(" ");
        final String longitude = c1.substring(pos_long+1);

        int i1;
        for (i1=pos_long-1;i1>0;i1--)
        {
            if(c1.charAt(i1)==' ')
                break;
        }

        final String latitude = c1.substring(i1+1,pos_long);
        final String city = c1.substring(0,i1);

        //String kk[]=c1.split(" ");
        //final String city=kk[0]+" "+kk[1]+" "+kk[2];
        //String latitude=kk[3];
        //String longitude=kk[4];

        Log:d("Tag :: ", latitude + " " + longitude);

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String wind_speed, String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                String final_temperature, final_pressure, final_windspeed;

                SharedPreferences ex1 = getSharedPreferences(PREFS2, 0);
                String c = ex1.getString("temp_scale", "not found");
                if(c.equals("not found")) {
                    SharedPreferences.Editor editor = ex1.edit();
                    editor.putString("temp_scale", "Celsius");
                    editor.commit();
                }

                SharedPreferences ex2 = getSharedPreferences(PREFS2, 0);
                String c1=ex2.getString("temp_scale", "not found");

                if(c1.equals("Celsius"))
                    final_temperature = weather_temperature + "°C";
                else if(c1.equals("Fahrenheit"))
                    final_temperature = String.format("%.2f", Double.parseDouble(weather_temperature)*9/5 + 32) + "°F";
                else
                    final_temperature = String.format("%.2f", Double.parseDouble(weather_temperature) + 273) + " K";

                SharedPreferences ex3 = getSharedPreferences(PREFS3, 0);
                String c2 = ex3.getString("pressure_scale", "not found");
                if(c2.equals("not found")) {
                    SharedPreferences.Editor editor1 = ex3.edit();
                    editor1.putString("pressure_scale", "hPa");
                    editor1.commit();
                }

                SharedPreferences ex4 = getSharedPreferences(PREFS3, 0);
                String c3=ex4.getString("pressure_scale", "not found");

                if(c3.equals("hPa"))
                    final_pressure = weather_pressure + " hPa";
                else if(c3.equals("atm"))
                    final_pressure = String.format("%.4f", Double.parseDouble(weather_pressure) * 0.00098692326) + " atm";
                else if(c3.equals("torr"))
                    final_pressure = String.format("%.4f", Double.parseDouble(weather_pressure) * 0.750061683) + " torr";
                else if(c3.equals("bar"))
                    final_pressure = String.format("%.4f", Double.parseDouble(weather_pressure) * 0.001) + " bar";
                else
                    final_pressure = String.format("%.4f", Double.parseDouble(weather_pressure) * 0.7500616827) + " mmHg";

                SharedPreferences ex5 = getSharedPreferences(PREFS4, 0);
                String c4 = ex5.getString("windspeed_scale", "not found");
                if(c4.equals("not found")) {
                    SharedPreferences.Editor editor1 = ex5.edit();
                    editor1.putString("windspeed_scale", "km/h");
                    editor1.commit();
                }

                SharedPreferences ex6 = getSharedPreferences(PREFS4, 0);
                String c5=ex6.getString("windspeed_scale", "not found");

                if(c5.equals("km/h"))
                    final_windspeed = wind_speed + " km/h";
                else if(c5.equals("m/s"))
                    final_windspeed = String.format("%.4f", Double.parseDouble(wind_speed) * 0.3) + " m/s";
                else if(c5.equals("mph"))
                    final_windspeed = String.format("%.4f", Double.parseDouble(wind_speed) * 0.6) + " mph";
                else
                    final_windspeed = String.format("%.4f", Double.parseDouble(wind_speed) * 0.5) + " knots";

                windspeed.setText("Wind speed: " + final_windspeed);
                cityField.setText(city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(final_temperature);
                humidity_field.setText("Humidity: " + weather_humidity);
                pressure_field.setText("Pressure: " + final_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

                SharedPreferences mg1 = getSharedPreferences(PREFS1, 0);
                SharedPreferences.Editor editor = mg1.edit();
                editor.putString("weather", "Location : "+city+"\nWeather : "+weather_description+"\nTemperature : "
                        +weather_temperature+"\nHumidity : "+weather_humidity+"\nPressure : "+weather_pressure);
                editor.commit();
            }
        });
        asyncTask.execute(latitude, longitude); //  asyncTask.execute("Latitude", "Longitude")

        // Refresh button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //int zxc = city.indexOf(',');
                z[0]=city;

                new GetLocation().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location != null)
            {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
            else
            {
                Log.d("TAG :: ", "Unable to find current location.");
                currentLatitude=0.0;
                currentLongitude=0.0;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent12 = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent12);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_current_location) {

            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            getLocation();

            Log.d("TAG :: current", currentLatitude+" "+currentLongitude);

            if(currentLatitude == 0.0 && currentLongitude == 0.0)
            {
                Toast.makeText(getApplicationContext(),
                        "Please turn on the GPS!",
                        Toast.LENGTH_LONG)
                        .show();
            }
            else {
                z[0] = String.valueOf(currentLatitude) + " " + String.valueOf(currentLongitude);

                new GetLocation().execute();
            }
        } else if (id == R.id.nav_change_location) {

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            final View mView = getLayoutInflater().inflate(R.layout.custom_layout, null);
            final EditText answer = (EditText) mView.findViewById(R.id.editText);
            Button ok = (Button) mView.findViewById(R.id.button);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    z[0] = answer.getText().toString();

                    new GetLocation().execute();

                    dialog.dismiss();
                }
            });

        }  else if (id == R.id.nav_manage) {

            Intent intent12 = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent12);

        } else if (id == R.id.nav_share) {

            SharedPreferences mg1 = getSharedPreferences(PREFS1, 0);
            String l1 = mg1.getString("weather", "not found");

            String shareBody = l1+"\n\nData fetched from Weatherly app.";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Weather Report -");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share weather report via"));

        } else if (id == R.id.nav_send) {

            ApplicationInfo api = getApplicationContext().getApplicationInfo();
            String apkpath = api.sourceDir;
            Intent share_intent = new Intent(Intent.ACTION_SEND);
            share_intent.setType("application/vnd.android.package-archive");
            share_intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
            startActivity(Intent.createChooser(share_intent, "Share app using"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetLocation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            url = "http://maps.google.com/maps/api/geocode/json?address="+z[0]+"&sensor=false";

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray results = jsonObj.getJSONArray("results");
                    Log:d("TAG :: ", String.valueOf(results.length()));
                    // Getting JSON Array node
                    JSONObject c1 = results.getJSONObject(0);
                    String location = c1.getString("formatted_address");

                    JSONObject c2 = c1.getJSONObject("geometry");
                    JSONObject c3 = c2.getJSONObject("location");


                    String lat = c3.getString("lat");
                    String lng = c3.getString("lng");

                    SharedPreferences ex3 = getSharedPreferences(PREFS, 0);
                    String c = ex3.getString("message", "not found");
                    SharedPreferences.Editor editor1 = ex3.edit();
                    editor1.putString("message", location+" "+lat+" "+lng);
                    editor1.commit();

                }
                catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),
                            //        "Json parsing error: " + e.getMessage(),
                            //        Toast.LENGTH_LONG)
                            //        .show();
                            Log.d("TAG ::","Json parsing error: "+e.getMessage());

                            Toast.makeText(getApplicationContext(),
                                    "Some error occured. Please try again!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),
                        //      "Couldn't get json from server. Check LogCat for possible errors!",
                        //        Toast.LENGTH_LONG)
                        //        .show();
                        Toast.makeText(getApplicationContext(),
                                "Please check your internet connetion!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Intent intent100 = getIntent();
            finish();
            startActivity(intent100);

            /**
             * Updating parsed JSON data into ListView
             * */
        }
    }
}

