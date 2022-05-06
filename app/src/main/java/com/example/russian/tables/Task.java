package com.example.russian.tables;

public class Task {
    private Integer _id;
    private String condition;

    public Task(){
    }

    public Task(int id ,String condition){
        this._id = id;
        this.condition = condition;
    }

    public Integer getID(){
        return this._id;
    }

    public void setID(Integer id){
        this._id = id;
    }

    public String getCondition(){
        return this.condition;
    }

    public void setCondition(String condition){
        this.condition = condition;
    }

}
