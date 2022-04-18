package com.example.smartbox_app;

public class Delivery {
    //init
    private String Date_Time;
    private String trackno;
    private String type;
    private String weight;
    private String name;
    private int id; //used for desc order

    public Delivery(){
        //leave blank for firebase
    }

    public Delivery(String Date_Time, String trackno, String type, String weight, int id, String name){
        this.Date_Time = Date_Time;
        this.trackno = trackno;
        this.type = type;
        this.weight = weight;
        this.id = id;
        this.name = name;
    }

    //getter and setter
    public String getDate_Time(){
        return Date_Time;
    }
    public String getTrackno(){
        return "Tracking No: " + trackno;
    }
    public String getType(){
        return "Type: "+type;
    }
    public String getWeight(){
        return "Weight: " + weight;
    }
    public String getName(){return "Courier: " + name;}
    public int getId(){
        return id;
    }

    public void setDate_Time(String Date_Time){
        this.Date_Time = Date_Time;
    }
    public void setTrackno(String trackno){
        this.trackno = trackno;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }
}



