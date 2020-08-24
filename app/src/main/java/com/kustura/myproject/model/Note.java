package com.kustura.myproject.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kustura.myproject.R;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private int priority;

    private String imgPath;

    public Note(String title, String description, int priority,String imgPath) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.imgPath = imgPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getImgPath() {
        return imgPath;
    }
}
