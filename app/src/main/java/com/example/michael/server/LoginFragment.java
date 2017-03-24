package com.example.michael.server;

import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.Model.ServerProxy;
import com.example.Model.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbrad94 on 3/20/17.
 */

public class LoginFragment extends Fragment {
    private User mUser;
    private EditText mUsernameIn;
    private EditText mPasswordIn;
    private EditText mPortIn;
    private EditText mHostIn;
    private EditText mFirstNameIn;
    private EditText mLastNameIn;
    private EditText mEmailIn;
    private Button mLoginButton;
    private Button mRegisterButton;
    private RadioGroup mGenderButton;
    private String mPort = "";
    private String mHost = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        hostInitializer(view);
        portInitializer(view);
        usernameInitializer(view);
        passwordInitializer(view);
        firstNameInitializer(view);
        lastNameInitializer(view);
        genderButtonInitializer(view);
        loginInitializer(view);
        registerInitializer(view);
        return view;
    }
    private void firstNameInitializer(View view){
        mFirstNameIn = (EditText) view.findViewById(R.id.firstname_field);
        mFirstNameIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mUser.setFirstname(mFirstNameIn.getText().toString());
                String temp = null;
            }
        });
    }
    private void lastNameInitializer(View view){
        mLastNameIn = (EditText) view.findViewById(R.id.lastname_field);
        mLastNameIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mUser.setLastname(mLastNameIn.getText().toString());
                String temp = null;
            }
        });
    }
    private void usernameInitializer(View view){
        mUsernameIn = (EditText) view.findViewById(R.id.username_field);
        mUsernameIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mUser.setUsername(mUsernameIn.getText().toString());
                String temp = null;
            }
        });
    }
    private void passwordInitializer(View view){
        mPasswordIn = (EditText) view.findViewById(R.id.password_field);
        mPasswordIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mUser.setPassword(mPasswordIn.getText().toString());
                String temp = null;
            }
        });
    }
    private void emailInitializer(View view){
        mEmailIn = (EditText) view.findViewById(R.id.email_field);
        mEmailIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mUser.setEmail(mEmailIn.getText().toString());
                String temp = null;
            }
        });
    }
    private void portInitializer(View view){
        mPortIn = (EditText) view.findViewById(R.id.port_field);
        mPortIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mPort = mPortIn.getText().toString();
                String temp = null;
            }
        });
    }
    private void hostInitializer(View view){
        mHostIn = (EditText) view.findViewById(R.id.host_field);
        mHostIn.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mHost = mHostIn.getText().toString();
                String temp = null;
            }
        });
    }
    private void genderButtonInitializer(View view){
        mGenderButton = (RadioGroup)view.findViewById(R.id.sex_button);
        mGenderButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Integer checked = checkedId;
                int manId = 2131427423;
                //male = 2131427423
                //female = 2131427424
                RadioButton genderButton = (RadioButton) group.findViewById(checkedId);
                if(null!=genderButton && checkedId > -1){
                    if(checked.equals(2131427423)){
                        mUser.setGender("m");
                        makeToast("Man");
                    }else{
                        mUser.setGender("w");
                        makeToast("Woman");
                    }
                }else{
                    makeToast("You must be an man or a woman, not an apache attack helicopter");
                }

            }
        });
    }
    private void loginInitializer(View view){
        mLoginButton = (Button) view.findViewById(R.id.submit_info);
        mLoginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkLoginInfo();
            }
        });

    }
    private void checkLoginInfo(){
        if(voidOrEmptyString(mPort)){
            makeToast("Please enter a port");
        }else if(voidOrEmptyString(mHost)){
            makeToast("Please enter a host");
        }else if(voidOrEmptyString(mUser.getUsername())){
            makeToast("Please enter a username");
        }else if(voidOrEmptyString(mUser.getPassword())){
            makeToast("Please enter a password");
        }else{
            try {
                LoginTask loginTask = new LoginTask();

                loginTask.execute(new URL("http://localhost:8080/"));
                //run an advanced check using the database and queries
            } catch (MalformedURLException e) {
                makeToast("Exception was thrown");
            }
        }
    }
    private void registerInitializer(View view){
        mRegisterButton = (Button) view.findViewById(R.id.register_info);
        mRegisterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkRegisteringInfo();
            }
        });
    }
    public void checkRegisteringInfo(){

        if(voidOrEmptyString(mPort)){
            makeToast("Please enter a port");
        }else if(voidOrEmptyString(mHost)){
            makeToast("Please enter a host");
        }else if(voidOrEmptyString(mUser.getUsername())){
            makeToast("Please enter a username");
        }else if(voidOrEmptyString(mUser.getPassword())){
            makeToast("Please enter a password");
        }else if(voidOrEmptyString(mUser.getFirstname())){
            makeToast("Please enter a firstname");
        }else if(voidOrEmptyString(mUser.getLastname())){
            makeToast("Please enter a lastname");
        }else if(voidOrEmptyString(mUser.getGender())){
            makeToast("Sorry but you cannot be gender neutral");
        }else if(voidOrEmptyString(mUser.getEmail())){
            makeToast("Please enter an email");
        }else {
            makeToast("Register the user");
        }
    }
    private void makeToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
    private boolean voidOrEmptyString(String word){
        return (word == null || word.equals(""));
    }

    public class LoginTask extends AsyncTask<URL, Integer, Long>{
        private boolean invalidLogin = false;
        String output = "";
        protected Long doInBackground(URL... urls) {
            long l = 0;
            //what to do in the back ground
            loginBackGroundTask();
            return l;
        }
        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            //if there is an error pop a toast here
            if(invalidLogin){
                makeToast("Failed to login: \n" + output);
            }else{
                makeToast("Logged In");
            }
        }
        private void loginBackGroundTask(){
            ServerProxy serverProxy = new ServerProxy();
            output = serverProxy.login(mUser);
            if(!checkForAuthToken(output)){
                //bad
                invalidLogin = true;
            }else{
                //good
                //save the authToken
                serverProxy.setAuthCode(extractAuthID(output));
                //load the info
            }
            //if string has an authtoken it was successfull if not, send toast
        }

        //return true if there is an authID
        private boolean checkForAuthToken(String word){
            String authString = "authorizationCode";
            if(word == null || word.equals("")){
                return false;
            }
            return word.contains(authString);
        }
        //returns the authID
        private String extractAuthID(String string){

            Pattern MY_PATTERN = Pattern.compile("Code\":\"(.*)\"");
            Matcher m = MY_PATTERN.matcher(string);
            String s = "";
            while (m.find()) {
                s = m.group(1);
            }
            return s;
        }
    }
}

