package com.example.memoi.todo

import kotlin.Throws
import com.example.memoi.todo.Task.NullIntegrityException
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime

class TodoBuilder {
    var title: String?
    var description: String?
    var date: String?
    var time: String?
    var location: String?
    var url: String?

    constructor() {
        title = null
        description = null
        date = null
        time = null
        location = null
        url = null
    }

    constructor(
        title: String?,
        description: String?,
        date: LocalDate,
        time: LocalTime,
        location: String?,
        url: String?
    ) {
        this.title = title
        this.description = description
        this.date = date.toString()
        this.time = time.toString()
        this.location = location
        this.url = url
    }

    constructor(
        title: String?,
        description: String?,
        date: String?,
        time: String?,
        location: String?,
        url: String?
    ) {
        if (date != null) LocalDate.parse(date)
        if (time != null) LocalTime.parse(time)
        this.title = title
        this.description = description
        this.date = date
        this.time = time
        this.location = location
        this.url = url
    }

    fun setTitle(t: String?) {
        title = t
    }

    fun setDescription(d: String?) {
        description = d
    }

    fun setDate(date: LocalDate) {
        this.date = date.toString()
    }

    fun setDate(date: String?) {
        LocalDate.parse(date) // parsable checking
        this.date = date
    }

    fun setDate(y: Int, m: Int, d: Int) {
        this.setDate(LocalDate.of(y, m, d))
    }

    fun setTime(time: LocalTime) {
        this.time = time.toString()
    }

    fun setTime(time: String?) {
        LocalTime.parse(time)
        this.time = time
    }

    fun setTime(h: Int, m: Int) {
        this.setTime(LocalTime.of(h, m))
    }

    fun setLocation(location: String?) {
        this.location = location
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    @Throws(Exception::class)
    fun build(): Todo {
        if (title == null) {
            throw NullIntegrityException()
        }
        return Todo(title, description, date, time, location, url)
    } /* format of
     * "{title}, {description}, {time}, {date}, {location}\n"
     * "___", "___", "yyyy-MM-dd", "HH-mm", ???
     * all values has "null" if null
     */
    /*
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
    */
}