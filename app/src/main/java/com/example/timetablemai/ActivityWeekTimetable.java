package com.example.timetablemai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.timetablemai.databinding.ActivityWeekTimetableBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ActivityWeekTimetable extends AppCompatActivity {
    Toolbar toolbar;
    ActivityWeekTimetableBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        super.onCreate(savedInstanceState);
        binding = ActivityWeekTimetableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout mainLayout = findViewById(R.id.LinearLayout3);

        ArrayList<Discipline> discArrayList = new ArrayList<>();

        for(int i = 0;i< MainActivity.discName2.size();i++){

            Discipline disc = new Discipline(MainActivity.discName2.get(i), MainActivity.time_start.get(i), MainActivity.time_end.get(i), MainActivity.type.get(i),MainActivity.room.get(i), MainActivity.lector.get(i));
            discArrayList.add(disc);

        }

        ListAdapter listAdapter = new ListAdapter(ActivityWeekTimetable.this,discArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });

    }

}