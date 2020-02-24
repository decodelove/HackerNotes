package com.logincore.hackernotes.bean;

import java.io.Serializable;

public class NotesBean implements Serializable {
    private static final long serialVersionUID = -1836023552113929574L;

    public String timeNotes;
    public String NotesContent;

    public String getTimeNotes() {
        return timeNotes;
    }

    public void setTimeNotes(String timeNotes) {
        this.timeNotes = timeNotes;
    }

    public String getNotesContent() {
        return NotesContent;
    }

    public void setNotesContent(String notesContent) {
        NotesContent = notesContent;
    }
}
