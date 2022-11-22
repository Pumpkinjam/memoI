package com.example.memoi.todo;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.time.*;

public class TodoBuilder extends TaskBuilder {
    Object location;

    public final class NullIntegrityException extends RuntimeException {
        NullIntegrityException() {
            super("title cannot be null");
        }
    }

    public TodoBuilder() {
        super();
        location = null;
    }
    // lots of... constructors... for null proccessing
    public TodoBuilder(String title) {
        super(title);
        this.location = null;
    }

    public TodoBuilder(String title, String description) {
        super(title, description);
        this.location = null;
    }

    public TodoBuilder(String title, String description, LocalDate date) {
        super(title, description, date);
        this.location = null;
    }

    public TodoBuilder(String title, String description, LocalTime time) {
        super(title, description, time);
        this.location = null;
    }

    public TodoBuilder(String title, String description, LocalDate date, LocalTime time) {
        super(title, description, date, time);
        this.location = null;
    }

    public TodoBuilder(String title, String description, LocalDate date, LocalTime time, Object location) {
        super(title, description, date, time);
        this.location = location;
    }
/*
    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }
    public void setTiming(LocalDateTime timing) {
        this.timing = timing;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        if (this.time != null) {
            this.timing = LocalDateTime.of(date, this.time);
        }
    }

    public void setDate(int y, int m, int d) {
        this.setDate(LocalDate.of(y, m, d));
    }

    public void setDate(String s) {
        this.date = LocalDate.parse(s);
        if (this.time != null) {
            this.timing = LocalDateTime.of(date, this.time);
        }
    }

    public void setTime(LocalTime time) {
        this.time = time;
        if (this.date != null) {
            this.timing = LocalDateTime.of(this.date, time);
        }
    }

    public void setTime(int h, int m) {
        this.setTime(LocalTime.of(h, m));
    }

    public void setTime(String s) {
        this.time = LocalTime.parse(s);
        if (this.date != null) {
            this.timing = LocalDateTime.of(this.date, time);
        }
    }
*/
    @Override
    // return type : Todo_
    public Task build() throws Exception {
        if (this.title == null) {
            throw new NullIntegrityException();
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
            return new TodoBuilder("Error", "Occured.", LocalDate.now(), LocalTime.now());
        }
    }
}