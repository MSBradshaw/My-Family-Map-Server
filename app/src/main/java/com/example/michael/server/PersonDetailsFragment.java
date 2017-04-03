package com.example.michael.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Event;
import com.example.Model.Person;

import java.util.List;

/**
 * Created by Michael on 3/30/2017.
 */

public class PersonDetailsFragment extends Fragment {
    private RecyclerView mPersonRecyclerView;
    private Person mPerson;
    private MainActivity mainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_recycler_view, container, false);

        mPersonRecyclerView = (RecyclerView) view
                .findViewById(R.id.events);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    private void updateUI() {
        //you will need the list of the person's events

     //   mAdapter = new PersonAdapter(ClientModel.getInstance().personEvents.get( ClientModel.getCurrentPerson().getPersonID() ));

       // mPersonRecyclerView.setAdapter(mAdapter);
    }


}
