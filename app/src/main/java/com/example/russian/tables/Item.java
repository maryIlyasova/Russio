package com.example.russian.tables;

public class Item {
    private int _id;
    private String name;
    private Integer cost;

    public Item(){
    }

    public Item(int id, String name, Integer cost){
        this._id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getCost(){
        return this.cost;
    }

    public void setCost(int cost){
        this.cost = cost;
    }
}
