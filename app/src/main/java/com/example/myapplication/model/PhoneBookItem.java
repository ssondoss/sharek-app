package com.example.myapplication.model;

import androidx.annotation.Nullable;

public class PhoneBookItem implements Comparable{

    private String name;
    private String phone;
    private String category;

    public PhoneBookItem(String name, String phone, String category) {
        this.name = name;
        this.phone = phone;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public int compareTo(Object o) {
        return category.compareTo(((PhoneBookItem)o).category);
    }
}
