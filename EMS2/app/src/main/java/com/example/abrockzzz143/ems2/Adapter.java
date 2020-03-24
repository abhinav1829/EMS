package com.example.abrockzzz143.ems2;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Information> arrayList;

    int d, h, m, s;

    public Adapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
    }

    public void Add_Data(Information info) {
        arrayList.add(info);
        notifyItemInserted(arrayList.size() - 1);
    }

    public void Delete_Data(Information info){
        int pos = arrayList.indexOf(info);
        arrayList.remove(pos);
        notifyItemRemoved(pos);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.custom_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Information information = arrayList.get(i);

        myViewHolder.name.setText(information.Name);
        if (information.Direction_flag)
            myViewHolder.direction.setText("You owe");
        else
            myViewHolder.direction.setText("Owes you");
        myViewHolder.amount.setText("" + information.Amount);

        long cur_date = System.currentTimeMillis() / 1000;
        String str_date = new java.text.SimpleDateFormat("EEE, MMM d").format(new java.util.Date(cur_date * 1000));
        myViewHolder.date.setText(str_date);

        long cur_time = System.currentTimeMillis() / 1000;
        String str_time = new java.text.SimpleDateFormat("h:mm a").format(new java.util.Date(cur_time * 1000));
        myViewHolder.time.setText(str_time);

        d = information.Hours / 24;
        h = information.Hours % 24;
        m = information.Minutes;
        s = 0;

        if (information.Hours == 0 && information.Minutes == 0)
            myViewHolder.timer.setText("Timer not set");
        else {
            int t = d * 86400000 + h * 3600000 + m * 60000 + s * 1000;
            new CountDownTimer(t, 1000) {

                public void onTick(long millisUntilFinished) {
                    d = (int) (millisUntilFinished / 86400000);
                    millisUntilFinished %= 86400000;
                    h = (int) (millisUntilFinished / 3600000);
                    millisUntilFinished %= 3600000;
                    m = (int) (millisUntilFinished / 60000);
                    millisUntilFinished %= 60000;
                    s = (int) (millisUntilFinished / 1000);
                    millisUntilFinished %= 1000;
                    myViewHolder.timer.setText(d + ":" + h + ":" + m + ":" + s);
                }

                public void onFinish() {
                    myViewHolder.timer.setText("Time out!!!");
                }

            }.start();
        }

        myViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Delete_Data(information);
                Toast.makeText(context, "Transaction deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView direction;
        TextView date;
        TextView time;
        TextView amount;
        TextView timer;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            direction = (TextView) itemView.findViewById(R.id.direction);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            amount = (TextView) itemView.findViewById(R.id.amount);
            timer = (TextView) itemView.findViewById(R.id.timer);
            cardView = (CardView) itemView.findViewById(R.id.card_row);
        }
    }
}
