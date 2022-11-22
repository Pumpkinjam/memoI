package com.example.memoi.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task {

    // primary key for the Instance
    public String created;  // instanceNum NOT NULL, not displayed for user

    // in general case, this can't be null
    public String title;            // title NOT NULL

    public String description;

    LocalDateTime timing;
    LocalDate date;
    LocalTime time;


    public final class NullIntegrityException extends RuntimeException {
        NullIntegrityException() {
            super("title cannot be null");
        }
    }


    Task(String title, String description, LocalDate date, LocalTime time) {
        if (title == null) throw new Task.NullIntegrityException();

        // firebase database paths must not contain '.'
        this.created = LocalDateTime.now().toString().substring(2).replace('.', '_');
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.timing = (date != null && time != null) ? LocalDateTime.of(date, time) : null;
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
                "\nTiming: " + timing;
    }

    public String dateToString() {
        if (this.date == null) return "";
        return this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String timeToString() {
        if (this.time == null) return "";
        return this.time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
