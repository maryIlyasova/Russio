package com.example.russian.tables;

public class Reward {
    private int _id;
    private String name;
    private Integer id_unit;

    public Reward(){
    }

    public Reward(int id, String name, Integer id_unit){
        this._id = id;
        this.name = name;
        this.id_unit = id_unit;
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

    public Integer getIdUnit(){
        return this.id_unit;
    }

    public void setIdUnit(int id_unit){
        this.id_unit = id_unit;
    }
}
