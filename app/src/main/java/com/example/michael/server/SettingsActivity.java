package com.example.michael.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Michael on 4/3/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    private static MainActivity mainActivity;
    public static void setMainActivity(MainActivity mainActivity) {
        SettingsActivity.mainActivity = mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Spinner spinnerLife = (Spinner) findViewById(R.id.life_color);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLife.setAdapter(adapter);

        spinnerChanges(spinnerLife, Settings.getInstance().getLife_story_lines_color(), "life");

        Spinner spinnerFamily = (Spinner) findViewById(R.id.Family_color);
        ArrayAdapter<CharSequence> adapterFamily = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapterFamily.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFamily.setAdapter(adapterFamily);

        spinnerChanges(spinnerFamily, Settings.getInstance().getFamily_story_lines_color(), "family");

        Spinner spinnerSpouse = (Spinner) findViewById(R.id.spouse_line_color);
        ArrayAdapter<CharSequence> adapterSpouse = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapterFamily.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpouse.setAdapter(adapterFamily);

        spinnerChanges(spinnerSpouse, Settings.getInstance().getSpouse_lines_color(), "spouse");

        Spinner spinnerMapType = (Spinner) findViewById(R.id.map_type);
        ArrayAdapter<CharSequence> adapterMap = ArrayAdapter.createFromResource(this, R.array.map_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapType.setAdapter(adapterMap);

        spinnerChanges(spinnerMapType, Settings.getInstance().getMap_type(), "map");

        final Switch switchLife = (Switch) findViewById(R.id.life_switch);
        switchLife.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                makeToast("Give me Life");
                if(switchLife.getText().equals("OFF ")){
                    switchLife.setText("ON");
                    Settings.getInstance().setLife_story_lines(true);
                }else{
                    switchLife.setText("OFF ");
                    Settings.getInstance().setLife_story_lines(false);
                }

            }
        });

        final Switch switchFamily = (Switch) findViewById(R.id.family_switch);
        switchFamily.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                makeToast("Family's Can Be together forever");
                if(switchFamily.getText().equals("OFF ")){
                    switchFamily.setText("ON");
                    Settings.getInstance().setFamily_sotry_lines(true);
                }else{
                    switchFamily.setText("OFF ");
                    Settings.getInstance().setFamily_sotry_lines(false);
                }
            }
        });

        final Switch switchSpouse = (Switch) findViewById(R.id.spouse_switch);
        switchSpouse.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                makeToast("Spouse Soup");
                if(switchSpouse.getText().equals("OFF ")){
                    switchSpouse.setText("ON");
                    Settings.getInstance().setSpouse_lines(true);
                }else{
                    switchSpouse.setText("OFF ");
                    Settings.getInstance().setSpouse_lines(false);
                }
            }
        });

        LinearLayout resync = (LinearLayout) findViewById(R.id.resync);
        resync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast("Resyncing");
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.initSyncTask();
                loginFragment.syncTask.outsideCall = true;
                try{
                    loginFragment.syncTask.execute(new URL("http://localhost:8080/"));
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        LinearLayout logout = (LinearLayout) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast("Log out");
                //end everything thing and start a new fragment
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //add all of the settings based on what is currently outlined
        switchLife.setChecked(Settings.getInstance().isLife_story_lines());
        switchFamily.setChecked(Settings.getInstance().isFamily_sotry_lines());
        switchSpouse.setChecked(Settings.getInstance().isSpouse_lines());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.back:
                //function of stuff to do
                finish();
                return true;
            case R.id.double_back:
                //other function
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    private void spinnerChanges(final Spinner spinner, String color, final String type){

        ArrayAdapter spinnerLifeAdapter = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = spinnerLifeAdapter.getPosition(color);
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                makeToast("Toast");
                if(type.equals("life")){
                    Settings.getInstance().setLife_story_lines_color((String) spinner.getSelectedItem());
                }else if(type.equals("family")){
                    Settings.getInstance().setFamily_story_lines_color((String) spinner.getSelectedItem());
                }else if(type.equals("spouse")){
                    Settings.getInstance().setSpouse_lines_color((String) spinner.getSelectedItem());
                } else if(type.equals("map")){
                    Settings.getInstance().setMap_type((String) spinner.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                makeToast("Toast None");
            }

        });
    }
}
