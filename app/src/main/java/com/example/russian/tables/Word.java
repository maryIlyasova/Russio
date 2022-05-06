package com.example.russian.tables;

public class Word {
    private int _id;
    private Integer id_unit;
    private String task_value;
    private String correct_value;
    private Integer id_task;

    public Word(){
    }

    public Word(int id, Integer id_unit, String task_value,String correct_value, Integer id_task){
        this._id = id;
        this.id_unit = id_unit;
        this.task_value = task_value;
        this.correct_value = correct_value;
        this.id_task = id_task;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getTaskValue(){
        return this.task_value;
    }

    public void setTaskValue(String task_value){
        this.task_value = task_value;
    }
    public String getCorrectValue(){
        return this.correct_value;
    }

    public void setCorrectValue(String correct_value){
        this.correct_value = correct_value;
    }

    public Integer getIdUnit(){
        return this.id_unit;
    }

    public void setIdUnit(int id_unit){
        this.id_unit = id_unit;
    }
    public Integer getIdTask(){
        return this.id_task;
    }

    public void setIdTask(int id_task){
        this.id_task = id_task;
    }
}
