package com.example.memoi.todo;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.time.*;

public class TodoBuilder{

    String title;
    String description;
    String date;
    String time;
    String location;

    public TodoBuilder() {
        this.title = null;
        this.description = null;
        this.date = null;
        this.time = null;
        this.location = null;
    }

    // lots of... constructors... for null proccessing
    public TodoBuilder(String title) {
        this(title, null, (String)null, null, null);
    }

    public TodoBuilder(String title, String description) {
        this(title, description, (String)null, null, null);
    }

    public TodoBuilder(String title, String description, LocalDate date, LocalTime time) {
        this(title, description, date, time, null);
    }

    public TodoBuilder(String title, String description, LocalDate date, LocalTime time, String location) {
        this.title = title;
        this.description = description;
        this.date = date.toString();
        this.time = time.toString();
        this.location = location;
    }

    public TodoBuilder(String title, String description, String date, String time, String location) {
        if (date != null) LocalDate.parse(date);
        if (time != null) LocalTime.parse(time);

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
    }


    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }

    public void setDate(LocalDate date) {
        this.date = date.toString();
    }

    public void setDate(String date) {
        LocalDate.parse(date);  // parsable checking
        this.date = date;
    }

    public void setDate(int y, int m, int d) {
        this.setDate(LocalDate.of(y, m, d));
    }

    public void setTime(LocalTime time) {
        this.time = time.toString();
    }

    public void setTime(String time) {
        LocalTime.parse(time);
        this.time = time;
    }

    public void setTime(int h, int m) {
        this.setTime(LocalTime.of(h, m));
    }


    public Todo build() throws Exception {
        if (this.title == null) {
            throw new Task.NullIntegrityException();
        }
        return new Todo(this.title, this.description, this.date, this.time, this.location);
    }

    /* format of
     * "{title}, {description}, {time}, {date}, {location}\n"
     * "___", "___", "yyyy-MM-dd", "HH-mm", ???
     * all values has "null" if null
     */
    public static TodoBuilder of(String csv) {
        StringTokenizer st = new StringTokenizer(csv, ", ");
        //System.out.println(st.countTokens());
        ArrayList<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) tokens.add(st.nextToken());
        //System.out.println(tokens.size());
        try {
            String title = tokens.get(0);
            String desc = tokens.get(1); if (desc.equals("null")) desc = null;
            String date = tokens.get(2); if (date.equals("null")) date = null;
            String time = tokens.get(3); if (time.equals("null")) time = null;
            // todo: loc is always null
            // when the type of location decided, please change here
            String loc = tokens.get(4); if (loc.equals("null")) loc = null;

            return new TodoBuilder(title, desc,
                    date == null ? null : LocalDate.parse(date),
                    time == null ? null : LocalTime.parse(time),
                    null);
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println("TodoBuilder.of() failed. returning Error Todo");
            e.printStackTrace();
            return new TodoBuilder("Error", "Occured.", LocalDate.now(), LocalTime.now());
        }
    }
}