package com.example.michael.server;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;
import com.example.Model.Person;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        TextView firstname = (TextView) findViewById(R.id.firstname);
        firstname.setText(ClientModel.getCurrentPerson().getFirstname());
        TextView lastname = (TextView) findViewById(R.id.lastname);
        lastname.setText(ClientModel.getCurrentPerson().getLastname());
        RecyclerView viewEvents = (RecyclerView) findViewById(R.id.events);
        RecyclerView viewRelatives = (RecyclerView) findViewById(R.id.family);
        TextView gender = (TextView) findViewById(R.id.gender);
        if(ClientModel.getCurrentPerson().getGender().equals("m") || ClientModel.getCurrentPerson().getGender().equals("M")){
            gender.setText("Male");
        }else{
            gender.setText("Female");
        }
        int size = ClientModel.getInstance().personEvents.size();
        EventAdapter personAdapter = new EventAdapter(ClientModel.getInstance().personEvents.get(ClientModel.getCurrentPerson().getPersonID()),this);
        viewEvents.setAdapter(personAdapter);
        viewEvents.setLayoutManager(new LinearLayoutManager(this));
        //get the familyPerson list and Relations
        List<Person> family = new ArrayList();
        List<String> relations =  new ArrayList();
        generatePeopleAndRelations(family, relations);
        FamilyAdapter familyAdapter = new FamilyAdapter(family,relations,this);
        viewRelatives.setAdapter(familyAdapter);
        viewRelatives.setLayoutManager(new LinearLayoutManager(this));


    }
    private void generatePeopleAndRelations(List<Person> people, List<String> relations){
        Person mom = ClientModel.getInstance().getMother();
        Person dad = ClientModel.getInstance().getFather();
        Person kid = ClientModel.getInstance().getChild();
        Person spouse = ClientModel.getInstance().getSpouse();
        if(mom != null){
            people.add(mom);
            relations.add("Mother");
        }
        if(dad != null){
            people.add(dad);
            relations.add("Father");
        }
        if(kid != null){
            people.add(kid);
            relations.add("Child");
        }
        if(spouse != null) {
            people.add(spouse);
            if (spouse.getGender().equals("m") || spouse.getGender().equals("M")) {
                relations.add("Husband");
            } else {
                relations.add("Wife");
            }
        }
    }
    private class FamilyAdapter extends RecyclerView.Adapter<PersonActivity.FamilyHolder>{
        private List<Person> familyPerson;
        private List<String> familyRelation;
        private Context context;

        public FamilyAdapter(List<Person> fam, List<String> rel, Context context) {
            familyPerson = fam;
            familyRelation = rel;
            this.context = context;
        }
        @Override
        public PersonActivity.FamilyHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            layoutInflater.inflate(R.layout.single_person_event,parent,false);
            return new PersonActivity.FamilyHolder(layoutInflater, parent);
        }
        @Override
        public int getItemCount() {
            return familyPerson.size();
        }

        @Override
        public void onBindViewHolder(PersonActivity.FamilyHolder holder, int position) {
            Person person = familyPerson.get(position);
            String relation = familyRelation.get(position);
            holder.bind(person, relation);
        }
    }
    private class EventAdapter extends RecyclerView.Adapter<PersonActivity.EventHolder> {

        private List<Event> mEvents;
        private Context context;

        public EventAdapter(List<Event> crimes, Context context) {
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
    private class FamilyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView relation;
        public void bind(Person person, String relationString){
            name.setText(person.getFirstname() + " " + person.getLastname());
            relation.setText(relationString);
        }
        public FamilyHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.single_person, parent, false));
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            relation = (TextView) itemView.findViewById(R.id.relation);
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
