package com.mixer.app;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends Activity {

    public static final String SELECTION1="com.mixer.app.SELECTION1";
    public static final String SELECTION2="com.mixer.app.SELECTION2";
    public static final String SELECTION3="com.mixer.app.SELECTION3";
    public static final String SELECTION4="com.mixer.app.SELECTION4";
    Spinner taste1, taste2, alc1, alc2;
    String selection1="", selection2="", selection3="", selection4="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch(SQLException sqle){
            throw sqle;
        }
        myDbHelper.close();

        taste1 = (Spinner) findViewById(R.id.spinner_taste1);
        alc1 = (Spinner)findViewById(R.id.spinner_alc1);
        populateSpinner(1);
        populateSpinner(2);
        addListenerToSpinner();
    }

    private void addListenerToSpinner() {

        alc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                alc2 = (Spinner)findViewById(R.id.spinner_alc2);
                TextView text2 = (TextView)findViewById(R.id.and_text2);
                selection4="";

                if (String.valueOf(alc1.getSelectedItem()).equals("Any") == false) {
                    text2.setVisibility(View.VISIBLE);
                    alc2.setVisibility(View.VISIBLE);
                }
                if (String.valueOf(alc1.getSelectedItem()).equals("Any") == true) {
                    text2.setVisibility(View.INVISIBLE);
                    alc2.setVisibility(View.INVISIBLE);
                }

                pos = alc1.getSelectedItemPosition();
                List<String> items = new ArrayList<String>(Arrays.asList(view.getContext().getResources().getStringArray(R.array.alcohol_type)));
                items.add(1,"None");
                items.remove(pos+1);

                ArrayAdapter<String> list = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item,items);
                list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                alc2.setAdapter(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        taste1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                taste2 = (Spinner) findViewById(R.id.spinner_taste2);
                TextView text = (TextView)findViewById(R.id.and_text);
                selection2="";

                if (String.valueOf(taste1.getSelectedItem()).equals("Any") == false) {
                    text.setVisibility(View.VISIBLE);
                    taste2.setVisibility(View.VISIBLE);
                }
                if (String.valueOf(taste1.getSelectedItem()).equals("Any") == true) {
                    text.setVisibility(View.INVISIBLE);
                    taste2.setVisibility(View.INVISIBLE);
                }

                pos = taste1.getSelectedItemPosition();
                List<String> items = new ArrayList<String>(Arrays.asList(view.getContext().getResources().getStringArray(R.array.taste_type)));
                items.add(1,"None");
                items.remove(pos+1);
                items.remove(0);

                ArrayAdapter<String> list = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item,items);
                list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                taste2.setAdapter(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void buttonClicked (View v){

        selection1 = String.valueOf(taste1.getSelectedItem());
        if (taste2.getVisibility()== View.VISIBLE){
            selection2 = String.valueOf(taste2.getSelectedItem());
        }
        selection3 = String.valueOf(alc1.getSelectedItem());
        if (alc2.getVisibility()==View.VISIBLE){
            selection4 = String.valueOf(alc2.getSelectedItem());
        }

        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra(SELECTION1,selection1);
        intent.putExtra(SELECTION2,selection2);
        intent.putExtra(SELECTION3,selection3);
        intent.putExtra(SELECTION4,selection4);

        startActivity(intent);

    }

    public void populateSpinner (int kind) {
        switch (kind) {
            case 1:
                ArrayAdapter<CharSequence> tasteList = ArrayAdapter.createFromResource(this, R.array.taste_type, android.R.layout.simple_spinner_item);
                tasteList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                taste1.setAdapter(tasteList);
                //taste2.setAdapter(tasteList);
                break;
            case 2:
                ArrayAdapter<CharSequence> alcList = ArrayAdapter.createFromResource(this, R.array.alcohol_type, R.layout.spinner_item);
                //alcList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                alc1.setAdapter(alcList);
                break;
            default:
                break;
        }

    }
}