package com.example.michael.server;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;

import java.util.List;

/**
 * Created by Michael on 3/30/2017.
 */

public class PersonActivity extends AppCompatActivity {
    private static MainActivity mainActivity;

    public static void setMainActivity(MainActivity mainActivity) {
        PersonActivity.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

        FragmentManager fm = getSupportFragmentManager();

        RecyclerView viewRC = (RecyclerView) findViewById(R.id.person_recycler_view);
        PersonAdapter personAdapter = new PersonAdapter(ClientModel.getInstance().personEvents.get(ClientModel.getCurrentPerson()),this);

    }
    private class PersonAdapter extends RecyclerView.Adapter<PersonActivity.EventHolder> {

        private List<Event> mEvents;
        private Context context;

        public PersonAdapter(List<Event> crimes, Context context) {
            mEvents = crimes;
            this.context = context;
        }
        @Override
        public PersonActivity.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            layoutInflater.inflate(R.layout.single_person_event,parent,false);
            return new PersonActivity.EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PersonActivity.EventHolder holder, int position) {
            Event event = mEvents.get(position);
            //holder.mEventTextView.setText("stuff");
            //holder.mNameTextView.setText("name");
            holder.bind(event);

        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }
    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private TextView mEventTextView;
        private TextView mNameTextView;
        //sets the information for the(event)
        public void bind(Event event) {
            mEvent = event;
            //event stuff
            String eventString = mEvent.getDescription() + ": " + mEvent.getCity() +
                    ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")";
            mEventTextView.setText(eventString);
            //name
            String name = ClientModel.getInstance().people.get(mEvent.getPersonID()).getFirstname() +
                    " " + ClientModel.getInstance().people.get(mEvent.getPersonID()).getLastname();
            mNameTextView.setText(name);
        }
        //initialize the fields to be updated
        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.single_person_event, parent, false));
            itemView.setOnClickListener(this);
            mEventTextView = (TextView) itemView.findViewById(R.id.event_info);
            mNameTextView = (TextView) itemView.findViewById(R.id.person_info);
        }

        @Override
        public void onClick(View view) {
            makeToast("McClick!");
        }
    }
    private void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
