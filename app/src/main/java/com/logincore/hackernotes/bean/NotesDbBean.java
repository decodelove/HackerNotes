package com.logincore.hackernotes.bean;

import java.io.Serializable;

public class NotesDbBean implements Serializable {

    private static final long serialVersionUID = 2483013966842257431L;

    public int id;
    public String time;
    public String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
