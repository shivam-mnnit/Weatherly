package com.example.dell.weatherly;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.example.dell.weatherly.MainActivity.PREFS2;
import static com.example.dell.weatherly.MainActivity.PREFS3;
import static com.example.dell.weatherly.MainActivity.PREFS4;


public class SettingsActivity extends AppCompatActivity {

    Button temperature_scale, pressure_scale, windspeed_scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        temperature_scale = (Button) findViewById(R.id.temperature_change);
        temperature_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.temperature_scale, null);

                final RadioGroup radioGroupTemperature = (RadioGroup) mView.findViewById(R.id.radioGroup_temperature);
                Button okTemperature = (Button) mView.findViewById(R.id.ok_temperature);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                okTemperature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // get selected radio button from radioGroup
                        int selectedId = radioGroupTemperature.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton radioButtonTempertaure = (RadioButton) mView.findViewById(selectedId);

                        SharedPreferences ex1 = getSharedPreferences(PREFS2, 0);
                        String c = ex1.getString("temp_scale", "not found");
                        SharedPreferences.Editor editor = ex1.edit();
                        editor.putString("temp_scale", String.valueOf(radioButtonTempertaure.getText()));
                        editor.commit();

                        Toast.makeText(SettingsActivity.this,
                                "Temperature scale set to " + radioButtonTempertaure.getText() + ".", Toast.LENGTH_SHORT).show();


                        dialog.dismiss();
                    }
                });
            }
        });

        pressure_scale = (Button) findViewById(R.id.pressure_change);
        pressure_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.pressure_scale, null);

                final RadioGroup radioGroupPressure = (RadioGroup) mView.findViewById(R.id.radioGroup_pressure);
                Button okTemperature = (Button) mView.findViewById(R.id.ok_pressure);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                okTemperature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // get selected radio button from radioGroup
                        int selectedId = radioGroupPressure.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton radioButtonPressure = (RadioButton) mView.findViewById(selectedId);

                        SharedPreferences ex1 = getSharedPreferences(PREFS3, 0);
                        String c = ex1.getString("pressure_scale", "not found");
                        SharedPreferences.Editor editor = ex1.edit();
                        editor.putString("pressure_scale", String.valueOf(radioButtonPressure.getText()));
                        editor.commit();

                        Toast.makeText(SettingsActivity.this,
                                "Pressure scale set to " + radioButtonPressure.getText() + ".", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
            }
        });

        windspeed_scale = (Button) findViewById(R.id.windspeed_change);
        windspeed_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.windspeed_scale, null);

                final RadioGroup radioGroupWindSpeed = (RadioGroup) mView.findViewById(R.id.radioGroup_windspeed);
                Button okWindSpeed = (Button) mView.findViewById(R.id.ok_windspeed);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                okWindSpeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // get selected radio button from radioGroup
                        int selectedId = radioGroupWindSpeed.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton radioButtonWindSpeed = (RadioButton) mView.findViewById(selectedId);

                        SharedPreferences ex1 = getSharedPreferences(PREFS4, 0);
                        String c = ex1.getString("windspeed_scale", "not found");
                        SharedPreferences.Editor editor = ex1.edit();
                        editor.putString("windspeed_scale", String.valueOf(radioButtonWindSpeed.getText()));
                        editor.commit();

                        Toast.makeText(SettingsActivity.this,
                                "Wind speed scale set to " + radioButtonWindSpeed.getText() + ".", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
            }
        });
    }
}