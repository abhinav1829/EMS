package com.example.abrockzzz143.ems2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Group extends AppCompatActivity {

    boolean creditor, present;
    int count, s, D, H, M, T;
    float myDif;

    LinearLayout linearLayout;
    Button add_group_button, add_field_button;
    Switch aSwitch;
    TextView gdays, ghours, gminutes;
    EditText enter_gdays, enter_ghours, enter_gminutes;

    ArrayList<Field> debtors, creditors;
    ArrayList<Information> information;
    Information info;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_main);

        linearLayout = (LinearLayout) findViewById(R.id.inside_scrollView);
        add_field_button = (Button) findViewById(R.id.add_field_button);
        add_group_button = (Button) findViewById(R.id.add_group_button);
        aSwitch = (Switch) findViewById(R.id.switch2);
        gdays = (TextView) findViewById(R.id.gdays);
        ghours = (TextView) findViewById(R.id.ghours);
        gminutes = (TextView) findViewById(R.id.gminutes);
        enter_gdays = (EditText) findViewById(R.id.enter_gdays);
        enter_ghours = (EditText) findViewById(R.id.enter_ghours);
        enter_gminutes = (EditText) findViewById(R.id.enter_gminutes);

        creditor = false;
        present = false;

        debtors = new ArrayList<>();
        creditors = new ArrayList<>();

        gdays.setVisibility(View.GONE);
        ghours.setVisibility(View.GONE);
        gminutes.setVisibility(View.GONE);
        enter_gdays.setVisibility(View.GONE);
        enter_ghours.setVisibility(View.GONE);
        enter_gminutes.setVisibility(View.GONE);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gdays.setVisibility(View.VISIBLE);
                    ghours.setVisibility(View.VISIBLE);
                    gminutes.setVisibility(View.VISIBLE);
                    enter_gdays.setVisibility(View.VISIBLE);
                    enter_ghours.setVisibility(View.VISIBLE);
                    enter_gminutes.setVisibility(View.VISIBLE);
                } else {
                    gdays.setVisibility(View.GONE);
                    ghours.setVisibility(View.GONE);
                    gminutes.setVisibility(View.GONE);
                    enter_gdays.setVisibility(View.GONE);
                    enter_ghours.setVisibility(View.GONE);
                    enter_gminutes.setVisibility(View.GONE);
                }
            }
        });

        add_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                present = false;

                if (!check_pays_is_owes()) return;

                if (!present) {
                    Toast.makeText(Group.this, "You have no transactions", Toast.LENGTH_SHORT).show();
                    debtors.clear();
                    creditors.clear();
                    return;
                }

                if (aSwitch.isChecked()) {
                    if (enter_gdays.getText().toString().length() == 0) D = 0;
                    else D = Integer.parseInt(enter_gdays.getText().toString());
                    if (enter_ghours.getText().toString().length() == 0) H = 0;
                    else H = Integer.parseInt(enter_ghours.getText().toString());
                    if (enter_gminutes.getText().toString().length() == 0) M = 0;
                    else M = Integer.parseInt(enter_gminutes.getText().toString());
                    if (D < 24 && H < 24 && M < 60) T = D * 24 + H;
                    else {
                        Toast.makeText(Group.this, "Enter valid time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    T = 0;
                    M = 0;
                }

                if (creditor) {
                    s = debtors.size();
                    float debDif = 0;
                    for (int i = 0; i < s; i++)
                        debDif += debtors.get(i).Owes - debtors.get(i).Pays;

                    information = new ArrayList<>();
                    for (int i = 0; i < s; i++) {
                        info = new Information();
                        info.Name = debtors.get(i).Name;
                        info.Direction_flag = false;
                        info.Amount = ((debtors.get(i).Owes - debtors.get(i).Pays) * myDif) / debDif;
                        info.Hours = T;
                        info.Minutes = M;
                        information.add(info);
                    }
                } else {
                    s = creditors.size();
                    float creDif = 0;
                    for (int i = 0; i < s; i++)
                        creDif += creditors.get(i).Pays - creditors.get(i).Owes;

                    information = new ArrayList<>();
                    for (int i = 0; i < s; i++) {
                        info = new Information();
                        info.Name = creditors.get(i).Name;
                        info.Direction_flag = true;
                        info.Amount = ((creditors.get(i).Pays - creditors.get(i).Owes) * myDif) / creDif;
                        info.Hours = T;
                        info.Minutes = M;
                        information.add(info);
                    }
                }

                intent = new Intent();
                intent.putExtra("AddTrans", information);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        add_field_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.field, null);
                linearLayout.addView(rowView, linearLayout.getChildCount() - 1);
            }
        });
    }

    public boolean check_pays_is_owes() {

        View view;
        EditText Name, Pays, Owes;

        Field field;

        String name;
        float pays, owes, sum_pays, sum_owes;

        sum_pays = 0;
        sum_owes = 0;

        count = linearLayout.getChildCount() - 1;

        for (int i = 0; i < count; i++) {

            view = linearLayout.getChildAt(i);

            Name = (EditText) view.findViewById(R.id.enter_name);
            name = Name.getText().toString();
            if (name.length() == 0) {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                return false;
            }

            Pays = (EditText) view.findViewById(R.id.enter_pays);
            if (Pays.getText().length() == 0) pays = 0;
            else pays = Float.parseFloat(Pays.getText().toString());

            Owes = (EditText) view.findViewById(R.id.enter_owes);
            if (Owes.getText().length() == 0) owes = 0;
            else owes = Float.parseFloat(Owes.getText().toString());


            field = new Field();
            field.Name = name;
            field.Pays = pays;
            field.Owes = owes;
            if (pays > owes) creditors.add(field);
            else if (pays < owes) debtors.add(field);

            if (name.matches("Abhinav")) {
                present = true;
                if (pays > owes) creditor = true;
                else if (pays < owes) creditor = false;
                else present = false;
                myDif = Math.abs(pays - owes);
            }

            sum_pays += pays;
            sum_owes += owes;
        }

        if (sum_pays == sum_owes) return true;
        else {
            Toast.makeText(this, "Transactions are unbalanced", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
