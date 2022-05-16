package com.example.russian.tables;

public class Class {
    private Integer _id;
    private String name;

    public Class(){
    }

    public Class(int id ,String name){
        this._id = id;
        this.name = name;
    }

    public Integer getID(){
        return this._id;
    }

    public void setID(Integer id){
        this._id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
