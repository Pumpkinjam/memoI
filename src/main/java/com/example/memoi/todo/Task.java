package com.example.memoi.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Task {

    // primary key for the Instance
    public String created;  // instanceNum NOT NULL, not displayed for user

    // in general case, this can't be null
    public String title;            // title NOT NULL

    public String description;

    /*LocalDate*/ String date;
    /*LocalTime*/ String time;


    public static final class NullIntegrityException extends RuntimeException {
        NullIntegrityException() {
            super("title cannot be null");
        }
    }

    Task(String title, String description, String date, String time) {
        // firebase database paths must not contain '.'
        this.created = LocalDateTime.now().toString().substring(2).replace('.', '_');

        // parsable String checking
        if (date != null) LocalDate.parse(date);
        if (time != null) LocalTime.parse(time);

        if (title == null) throw new Task.NullIntegrityException();

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    Task(String title, String description, LocalDate date, LocalTime time) {
        if (title == null) throw new Task.NullIntegrityException();

        // firebase database paths must not contain '.'
        this.created = LocalDateTime.now().toString().substring(2).replace('.', '_');
        this.title = title;
        this.description = description;
        this.date = date == null ? null : date.toString();
        this.time = time == null ? null : time.toString();
    }


    /* formatting to
     * "{title}, {description}, {time}, {date}, {location}\n"
     */
    // TODO: in csv, we can't save character ","...
    public String toCsvFormat() {
        String res = "";
        //res += instanceNum + ", ";
        res += title + ", ";
        res += (description == null ? "null" : description) + ", ";

        String strDate = dateToString();
        res += (strDate.equals("") ? "null" : strDate) + ", ";
        String strTime = timeToString();
        res += (strTime.equals("") ? "null" : strTime) + ", ";

        System.out.println(res);
        return res + "\n";
    }

    // get/set -ters
    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }

    // toString() Override
    @Override
    public String toString() {
        return "<Object Task : " + created + ">" +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nDate: " + date +
                "\nTime: " + time;
    }

    public String dateToString() {
        if (this.date == null) return "";
        return this.date;
    }

    public String timeToString() {
        if (this.time == null) return "";
        return this.time;
    }
}
