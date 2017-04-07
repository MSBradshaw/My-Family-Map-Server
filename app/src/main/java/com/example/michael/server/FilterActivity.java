package com.example.michael.server;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 4/6/2017.
 */

public class FilterActivity extends AppCompatActivity {
    private static MainActivity mainActivity;
    public static void setMainActivity(MainActivity mainActivity) {
        FilterActivity.mainActivity = mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        RecyclerView filterRecycler = (RecyclerView) findViewById(R.id.filter_recycler);
        FilterAdapter filterAdapter = new FilterAdapter();
        filterRecycler.setAdapter(filterAdapter);
        filterRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
    private class FilterAdapter extends RecyclerView.Adapter<FilterActivity.FilterHolder> {
        List<String> eventTypes;
        public FilterAdapter(){
            eventTypes = new ArrayList(ClientModel.getInstance().eventTypes);
        }
        @Override
        public void onBindViewHolder(FilterActivity.FilterHolder holder, int position) {

            String eventType = eventTypes.get(position);
            holder.bind(eventType);

        }

        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
            layoutInflater.inflate(R.layout.single_filter,parent,false);
            return new FilterActivity.FilterHolder(layoutInflater, parent);

        }

        @Override
        public int getItemCount() {
            return eventTypes.size();
        }
    }
    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mFilterTitle;
        TextView mFilterCaption;
        Switch mFilterSwitch;
        public FilterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.single_filter,parent,false));
            itemView.setOnClickListener(this);
            mFilterTitle = (TextView) itemView.findViewById(R.id.filter_title);
            mFilterCaption = (TextView) itemView.findViewById(R.id.filter_caption);
            mFilterSwitch = (Switch) itemView.findViewById(R.id.filter_switch);
        }
        public void bind(final String type){
            mFilterTitle.setText(type);
            mFilterCaption.setText("Filter by " + type);
            mFilterSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Settings.getInstance().filterSettings.put(type,isChecked);
                    if(isChecked){
                        mFilterSwitch.setText("ON");
                    }else{
                        mFilterSwitch.setText("OFF");
                    }
                }
            });
            mFilterSwitch.setChecked(true);
            int size = Settings.getInstance().filterSettings.size();
            mFilterSwitch.setText("ON");
        }

        @Override
        public void onClick(View v) {
            makeToast("Click it or Ticket");
        }
    }
    private void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
