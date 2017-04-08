package com.example.michael.server;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;
import com.example.Model.Person;

import java.util.List;

/**
 * Created by Michael on 4/8/2017.
 */

public class SearchActivity extends AppCompatActivity {
    private EditText searchField;
    private String searchString = "";
    private ImageView searchButton;
    private List<Person> people;
    private List<Event> events;
    private static MainActivity mainActivity;
    public static void setMainActivity(MainActivity mainActivity) {
        SearchActivity.mainActivity = mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        final EditText editText = (EditText) findViewById(R.id.search_field);
        View view = findViewById(R.id.search_activity);
        ImageView imageView = (ImageView) findViewById(R.id.search_button);
        initializeSearchButton(imageView);
        initilizeSearchField(editText);
    }
    private void initilizeSearchField(final EditText editText){
        editText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchString = editText.getText().toString();
            }
        });
    }
    private void initializeSearchButton(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast("Click Search");
                if(searchString.length() == 0){
                    makeToast("You must enter something to search for");
                    return;
                }
                //run the search functions and have them return two lists people and events
                events = ClientModel.getInstance().searchEvents(searchString);
                //apply the events to a recycler view
                RecyclerView eventsRecycler = (RecyclerView) findViewById(R.id.search_events);
                EventAdapter eventAdapter = new EventAdapter(events,getBaseContext());
                eventsRecycler.setAdapter(eventAdapter);
                eventsRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

                people = ClientModel.getInstance().searchPeople(searchString);
                RecyclerView personRecycler = (RecyclerView) findViewById(R.id.search_people);
                FamilyAdapter personAdapter = new FamilyAdapter(people,getBaseContext());
                personRecycler.setAdapter(personAdapter);
                personRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }
        });
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
    private class EventAdapter extends RecyclerView.Adapter<SearchActivity.EventHolder> {

        private List<Event> mEvents;
        private Context context;

        public EventAdapter(List<Event> events, Context context) {
            mEvents = events;
            sortEvents();
            this.context = context;
        }
        private void sortEvents(){
            List<Event> temp = mEvents;
            //birth first
            //death last
            //sorted by date or ABC if no date
            basicSort(temp);
            mEvents = temp;
        }
        private void basicSort(List<Event> temp){
            boolean changed = true;
            while(changed) {
                changed = false;
                for (int i = 0; i < temp.size() - 1; i++) {
                    if(temp.get(i).getYear() != null && !temp.get(i).getYear().equals("")
                            && temp.get(i+1).getYear() != null && !temp.get(i+1).getYear().equals("")
                            ){
                        int year1 = Integer.parseInt(temp.get(i).getYear());
                        int year2 = Integer.parseInt(temp.get(i + 1).getYear());
                        if (year1 > year2) {
                            //switch the two of them
                            Event e = temp.get(i);
                            temp.set(i,temp.get(i+1));
                            temp.set(i+1,e);
                            changed = true;
                        }
                    }else{
                        String first = temp.get(i).getDescription().toLowerCase();
                        String second = temp.get(i+1).getDescription().toLowerCase();
                        if(first.compareTo(second) > 0){
                            Event e = temp.get(i);
                            temp.set(i,temp.get(i+1));
                            temp.set(i+1,e);
                            changed = true;
                        }
                    }
                }
            }
        }
        @Override
        public SearchActivity.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            layoutInflater.inflate(R.layout.single_person_event,parent,false);
            return new SearchActivity.EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SearchActivity.EventHolder holder, int position) {
            Event event = mEvents.get(position);
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
            ImageView eventImg = (ImageView) itemView.findViewById(R.id.event_img);
            if(event.getDescription().equals("marriage")){
                eventImg.setImageResource(R.drawable.marriage);
            }else if(event.getDescription().equals("death")){
                eventImg.setImageResource(R.mipmap.death);
            }else if(event.getDescription().equals("birth")){
                eventImg.setImageResource(R.mipmap.birth);
            }
            //else the icon will stay as the generic event icon
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
            //start map activity focused on that event
            MapActivity.setMainActivity(mainActivity);
            MapActivity.setEvent(mEvent);
            Intent intent = new Intent(mainActivity, MapActivity.class);
            startActivity(intent);
            makeToast("McClick!");
            //start new So got map activity focused on that event
        }
    }
    private class FamilyAdapter extends RecyclerView.Adapter<SearchActivity.FamilyHolder>{
        private List<Person> familyPerson;
        private Context context;

        public FamilyAdapter(List<Person> fam, Context context) {
            familyPerson = fam;
            this.context = context;
        }
        @Override
        public SearchActivity.FamilyHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            layoutInflater.inflate(R.layout.single_person_event,parent,false);
            return new SearchActivity.FamilyHolder(layoutInflater, parent);
        }
        @Override
        public int getItemCount() {
            return familyPerson.size();
        }

        @Override
        public void onBindViewHolder(SearchActivity.FamilyHolder holder, int position) {
            Person person = familyPerson.get(position);
            holder.bind(person);
        }
    }
    private class FamilyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private Person person;
        public void bind(Person person){
            this.person = person;
            name.setText(person.getFirstname() + " " + person.getLastname());
            if(person.getGender().equals("m") || person.getGender().equals("M")){
                ImageView genderIcon = (ImageView) itemView.findViewById(R.id.person_icon);
                genderIcon.setImageResource(R.drawable.ic_father);
            }else{
                ImageView genderIcon = (ImageView) itemView.findViewById(R.id.person_icon);
                genderIcon.setImageResource(R.drawable.ic_mother);
            }
        }
        public FamilyHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.single_person, parent, false));
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
        }
        @Override
        public void onClick(View view) {
            //start a new person activity
            //change the current person...
            ClientModel.setCurrentPerson(person);
            PersonActivity.setMainActivity(mainActivity);
            Intent intent = new Intent(mainActivity, PersonActivity.class);
            startActivity(intent);
        }
    }
}
