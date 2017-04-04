package com.example.michael.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        Spinner spinner = (Spinner) findViewById(R.id.life_color);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinnerFamily = (Spinner) findViewById(R.id.Family_color);
        ArrayAdapter<CharSequence> adapterFamily = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapterFamily.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFamily.setAdapter(adapterFamily);

        Spinner spinnerSpouse = (Spinner) findViewById(R.id.spouse_line_color);
        ArrayAdapter<CharSequence> adapterSpouse = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapterFamily.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpouse.setAdapter(adapterFamily);


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
}
