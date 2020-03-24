package com.example.abrockzzz143.ems2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Add extends AppCompatActivity {

    String n;
    boolean d;
    float a;
    int D, H, M, T;

    Button button;
    TextView text_days, text_hours, text_minutes;
    EditText name, amount, days, hours, minutes;
    ToggleButton direction;
    Switch aSwitch;

    Information information;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_row_main);

        name = (EditText) findViewById(R.id.text_name);
        direction = (ToggleButton) findViewById(R.id.toggleButton_direction);
        amount = (EditText) findViewById(R.id.text_amount);
        days = (EditText) findViewById(R.id.enter_days);
        hours = (EditText) findViewById(R.id.enter_hours);
        minutes = (EditText) findViewById(R.id.enter_minutes);
        text_days = (TextView) findViewById(R.id.days);
        text_hours = (TextView) findViewById(R.id.hours);
        text_minutes = (TextView) findViewById(R.id.minutes);
        button = (Button) findViewById(R.id.add_button);
        aSwitch = (Switch) findViewById(R.id.switch1);

        text_days.setVisibility(View.INVISIBLE);
        text_hours.setVisibility(View.INVISIBLE);
        text_minutes.setVisibility(View.INVISIBLE);
        days.setVisibility(View.INVISIBLE);
        hours.setVisibility(View.INVISIBLE);
        minutes.setVisibility(View.INVISIBLE);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    text_days.setVisibility(View.VISIBLE);
                    text_hours.setVisibility(View.VISIBLE);
                    text_minutes.setVisibility(View.VISIBLE);
                    days.setVisibility(View.VISIBLE);
                    hours.setVisibility(View.VISIBLE);
                    minutes.setVisibility(View.VISIBLE);
                } else {
                    text_days.setVisibility(View.INVISIBLE);
                    text_hours.setVisibility(View.INVISIBLE);
                    text_minutes.setVisibility(View.INVISIBLE);
                    days.setVisibility(View.INVISIBLE);
                    hours.setVisibility(View.INVISIBLE);
                    minutes.setVisibility(View.INVISIBLE);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                n = name.getText().toString();
                if (direction.isChecked())
                    d = true;
                else
                    d = false;
                if (n.length() == 0 || amount.getText().toString().length() == 0) {
                    Toast.makeText(Add.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else
                    a = Float.parseFloat(amount.getText().toString());

                if (aSwitch.isChecked()) {
                    if (days.getText().toString().length() == 0)
                        D = 0;
                    else
                        D = Integer.parseInt(days.getText().toString());
                    if (hours.getText().toString().length() == 0)
                        H = 0;
                    else
                        H = Integer.parseInt(hours.getText().toString());
                    if (minutes.getText().toString().length() == 0)
                        M = 0;
                    else
                        M = Integer.parseInt(minutes.getText().toString());
                    if (D < 24 && H < 24 && M < 60)
                        T = D * 24 + H;
                    else {
                        Toast.makeText(Add.this, "Enter valid time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    T = 0;
                    M = 0;
                }

                information = new Information();
                information.Name = n;
                information.Direction_flag = d;
                information.Amount = a;
                information.Hours = T;
                information.Minutes = M;

                intent = new Intent();
                intent.putExtra("Name", n).putExtra("Direction_flag", d).putExtra("Amount", a).putExtra("Hours", T).putExtra("Minutes", M);
                if (aSwitch.isChecked())
                    setResult(Activity.RESULT_OK, intent);
                else
                    setResult(Activity.RESULT_CANCELED, intent);

                D = 0;
                H = 0;
                M = 0;
                T = 0;

                finish();
            }
        });
    }
}
