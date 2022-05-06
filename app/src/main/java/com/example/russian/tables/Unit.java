package com.example.russian.tables;

public class Unit {
    private int _id;
    private String name;
    private Integer id_class;

    public Unit(){
    }

    public Unit(int id, Integer id_class, String name){
        this._id = id;
        this.name = name;
        this.id_class = id_class;
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

    public Integer getIdClass(){
        return this.id_class;
    }

    public void setIdClass(int id_class){
        this.id_class = id_class;
    }
}
