/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Entite;

import java.util.Date;

/**
 *
 * @author kinto
 */
public class Event {
    private int id;
    private int club_id;
    private String name;
    private String description;
    private String date;
    private int duration;
    private String place;
    private String cover;

    public Event() {
    }

    public Event(int club_id, String name, String description, String date, int duration, String place, String cover) {
        this.club_id = club_id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.place = place;
        this.cover = cover;
    }

    public Event(int id, int club_id, String name, String description, String date, int duration, String place, String cover) {
        this.id = id;
        this.club_id = club_id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.place = place;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClub_id() {
        return club_id;
    }

    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", club_id=" + club_id + ", name=" + name + ", description=" + description + ", date=" + date + ", duration=" + duration + ", place=" + place + ", cover=" + cover + '}';
    }
    
}
