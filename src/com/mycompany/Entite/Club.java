/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Entite;

/**
 *
 * @author kinto
 */
public class Club {
    private int id;
    private String name;
    private String logo;
    private String description;
    private String type;
    private int moderator;

    public Club() {
    }

    public Club(String name, String logo, String description, String type, int moderator) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.type = type;
        this.moderator = moderator;
    }

    public Club(String name, String logo, String description, String type) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.type = type;
    }
    
    public Club(int id, String name, String logo, String description, String type, int moderator) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.type = type;
        this.moderator = moderator;
    }

    @Override
    public String toString() {
        return "Club{" + "id=" + id + ", name=" + name + ", logo=" + logo + ", description=" + description + ", type=" + type + ", moderator=" + moderator + '}';
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getModerator() {
        return moderator;
    }

    public void setModerator(int moderator) {
        this.moderator = moderator;
    }

}
