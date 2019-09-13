package com.poudel.taskmaster.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Date;


@DynamoDBDocument
public class History { ;
    private String date;
    private String action;

    public History(){}

    public History(String action) {
        this.date = new Date().toString();
        this.action = action;
    }

    @DynamoDBAttribute
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDBAttribute
    public  String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
