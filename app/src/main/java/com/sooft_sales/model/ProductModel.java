package com.sooft_sales.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.sooft_sales.tags.Tags;

import java.io.Serializable;

@Entity(tableName = Tags.table_products, indices ={ @Index(value = {"category_id", "id"}),@Index(value = {"title"},unique = true),@Index(value = { "title2"},unique = true),@Index(value = {"id"}, unique = true)},
        foreignKeys = {
                @ForeignKey(entity = DepartmentModel.class, parentColumns = "id", childColumns = "category_id", onDelete = CASCADE, onUpdate = CASCADE)

        }

)
public class ProductModel implements Serializable {
    @PrimaryKey
    private double id;
    private String title;
    private String title2;
    @Ignore
    private String photo;
    private int category_id;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] imageBitmap;
    private String type;
    private double price;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public byte[] getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(byte[] imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
