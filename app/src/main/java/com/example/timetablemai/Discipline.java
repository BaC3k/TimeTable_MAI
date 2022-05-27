package com.example.timetablemai;

public class Discipline {
    String name, start_time, end_time, type, room, lector;

    public Discipline(String name,String start_time, String end_time, String type, String room, String lector) {
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.type = type;
        this.room = room;
        this.lector = lector;
    }
}
