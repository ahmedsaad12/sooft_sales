package com.sooft_sales.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.sooft_sales.tags.Tags;

import java.io.Serializable;
@Entity(tableName = Tags.table_category,indices = {@Index(value = {"id"},unique = true)})

public class DepartmentModel implements Serializable {
    @PrimaryKey
    private int id;
   private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
