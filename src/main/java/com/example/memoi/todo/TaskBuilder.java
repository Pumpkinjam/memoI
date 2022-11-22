package com.example.memoi.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TaskBuilder {
    String title;
    String description;
    LocalDate date;
    LocalTime time;
    LocalDateTime timing;

    public final class NullIntegrityException extends RuntimeException {
        NullIntegrityException() {
            super("title cannot be null");
        }
    }

    public TaskBuilder() {
        this.title = null;
        this.description = null;
        this.date = null;
        this.time = null;
        this.timing = null;
    }
    // lots of... constructors... for null proccessing
    public TaskBuilder(String title) {
        this(title, null, null, null);
    }

    public TaskBuilder(String title, String description) {
        this(title, description, null, null);
    }

    public TaskBuilder(String title, String description, LocalDate date) {
        this(title, description, date, null);
    }

    public TaskBuilder(String title, String description, LocalTime time) {
        this(title, description, null, time);
    }

    public TaskBuilder(String title, String description, LocalDate date, LocalTime time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.timing = (date != null && time != null) ? LocalDateTime.of(date, time) : null;
    }

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

    public Task build() throws Exception {
        if (this.title == null) {
            throw new NullIntegrityException();
        }
        return new Task(this.title, this.description, this.date, this.time);
    }

    /* format of
     * "{title}, {description}, {time}, {date}, {location}\n"
     * "___", "___", "yyyy-MM-dd", "HH-mm", ???
     * all values has "null" if null
     */
    public static TaskBuilder of(String csv) {
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

            return new TaskBuilder(title, desc,
                    date == null ? null : LocalDate.parse(date),
                    time == null ? null : LocalTime.parse(time)
                    );
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println("TodoBuilder.of() failed. returning Error Todo");
            return new TaskBuilder("Error", "Occured.", LocalDate.now(), LocalTime.now());
        }
    }
}