package com.example.abrockzzz143.ems2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    Bundle bundle;
    Information info;
    ArrayList<Information> information, arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Add.class), 313);
            }
        });

        Button save = (Button) findViewById(R.id.saveButton);
        Button load = (Button) findViewById(R.id.loadButton);

        arrayList = new ArrayList<>();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(arrayList);
                editor.putString("transaction list", json);
                editor.apply();
                Toast.makeText(MainActivity.this, "Transactions Saved", Toast.LENGTH_SHORT).show();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("transaction list", null);
                Type type = new TypeToken<ArrayList<Information>>() {
                }.getType();
                arrayList = gson.fromJson(json, type);
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                for (Information inf : arrayList) {
                    adapter.Add_Data(inf);
                }
                Toast.makeText(MainActivity.this, "Transactions Loaded", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new Adapter(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 313) {
            if (data == null) return;
            bundle = data.getExtras();
            info = new Information();
            info.Name = bundle.getString("Name");
            info.Direction_flag = bundle.getBoolean("Direction_flag");
            info.Amount = bundle.getFloat("Amount");
            info.Hours = bundle.getInt("Hours");
            info.Minutes = bundle.getInt("Minutes");
            adapter.Add_Data(info);
            arrayList.add(info);

            if (resultCode == Activity.RESULT_OK) {
                if (info.Hours == 0 && info.Minutes == 0)
                    Toast.makeText(this, "Timer not set", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Timer set", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Timer not set", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Result code error", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 10) {
            if (data == null) return;
            bundle = data.getExtras();
            information = (ArrayList) bundle.getParcelableArrayList("AddTrans");
            for (int i = 0; i < information.size(); i++) {
                info = new Information();
                info.Name = information.get(i).Name;
                info.Direction_flag = information.get(i).Direction_flag;
                info.Amount = information.get(i).Amount;
                info.Hours = information.get(i).Hours;
                info.Minutes = information.get(i).Minutes;
                adapter.Add_Data(info);
                arrayList.add(info);
            }
            if (resultCode == Activity.RESULT_OK)
                Toast.makeText(this, "Transactions added", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Error adding transactions", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Request code error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_group) {
            startActivityForResult(new Intent(MainActivity.this, Group.class), 10);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
