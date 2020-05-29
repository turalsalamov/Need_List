package com.example.needlist.model;

public class Needs {

    private int id;
    private String name;
    private int quantity;
    private String size;
    private String note;

    public Needs(){

    }

    public Needs(String name, int quantity, String size, String note) {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getNote() {
        return note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
