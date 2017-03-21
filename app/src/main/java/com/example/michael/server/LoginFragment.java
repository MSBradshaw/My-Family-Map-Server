package com.example.michael.server;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Model.User;

/**
 * Created by mbrad94 on 3/20/17.
 */

public class LoginFragment extends Fragment{
    private User mUser;
    private EditText mUsernameIn;
    private EditText mPasswordIn;
    private Button mSubmitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewOfActivityMain = inflater.inflate(R.layout.fragment_login,container,false);
        mUsernameIn = (EditText) viewOfActivityMain.findViewById(R.id.username_field);
        mPasswordIn = (EditText) viewOfActivityMain.findViewById(R.id.password_field);
        if(mUsernameIn == null || mPasswordIn == null){
            return viewOfActivityMain;
        }

        mUsernameIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mUser.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mPasswordIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mUser.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mSubmitButton = (Button) viewOfActivityMain.findViewById(R.id.submit_info);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //checkAnswer(true);
                System.out.println("Clicked");

            }
        });
        mSubmitButton.setEnabled(false);

        return viewOfActivityMain;
    }
}
