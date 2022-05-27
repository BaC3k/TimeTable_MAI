package com.example.timetablemai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Discipline> {


    public ListAdapter(Context context, ArrayList<Discipline> userArrayList){

        super(context,R.layout.list_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Discipline discipline = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        TextView disciplineName = convertView.findViewById(R.id.disciplineName);
        TextView lector = convertView.findViewById(R.id.lector);
        TextView time = convertView.findViewById(R.id.time);
        TextView room = convertView.findViewById(R.id.room);
        TextView type = convertView.findViewById(R.id.type);

        disciplineName.setText(discipline.name);
        lector.setText(discipline.lector);
        time.setText(discipline.start_time.substring(0,(discipline.start_time.length()-3)) + " - " + discipline.end_time.substring(0,(discipline.end_time.length()-3)));
        room.setText(discipline.room);
        type.setText(discipline.type);
        return convertView;
    }
}
