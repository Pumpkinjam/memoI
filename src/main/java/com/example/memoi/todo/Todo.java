package com.example.memoi.todo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Todo extends Task {
    /*

    // in general case, this can't be null
    public String title;            // title NOT NULL

    public String description;

    private LocalDateTime timing;
    private LocalDate date;
    private LocalTime time;
    */

    // TODO: find appropriate class for location
    Object location;

    Todo(String title, String description, LocalDate date, LocalTime time, Object location) {
        /*
        if (title == null) throw new NullIntegrityException();

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.timing = (date != null && time != null) ? LocalDateTime.of(date, time) : null;
        this.location = location;
        */
        super(title, description, date, time);
        this.location = location;
    }


    /* formatting to
     * "{title}, {description}, {time}, {date}, {location}\n"
     */
    @Override
    public String toCsvFormat() {
        String res = "";
        //res += instanceNum + ", ";
        res += title + ", ";
        res += (description == null ? "null" : description) + ", ";

        String strDate = dateToString();
        res += (strDate.equals("") ? "null" : strDate) + ", ";
        String strTime = timeToString();
        res += (strTime.equals("") ? "null" : strTime) + ", ";

        res += location == null ? "null" : location.toString();

        System.out.println(res);
        return res + "\n";
    }

    // get/set -ters
    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }

    public void setTiming(LocalDateTime timing) {
        this.timing = timing;
    }

    public LocalDateTime getTiming() {
        return this.timing;
    }

    public LocalDate getDate() { return this.date; }

    public LocalTime getTime() { return this.time; }

    public void setDate(LocalDate date) {
        this.date = date;
        if (this.time != null) {
            this.timing = LocalDateTime.of(date, this.time);
        }
    }

    public void setDate(int y, int m, int d) {
        this.setDate(LocalDate.of(y, m, d));
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

    // toString() Override
    @Override
    public String toString() {
        return "<Object Todo : " + created + ">" +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nTiming: " + timing +
                "\nLocation" + location;
    }
/*
    public String dateToString() {
        if (this.date == null) return "";
        return this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String timeToString() {
        if (this.time == null) return "";
        return this.time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
*/
}
