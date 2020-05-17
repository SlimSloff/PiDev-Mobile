/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utils;

import com.mycompany.Entite.User;

/**
 *
 * @author Dell
 */
public class Session {
    static Session instance;
    User loggedInUser;
    
    private Session()
    {
        
    }

    public static Session getInstance() {
       if(instance == null)
        {
            instance = new Session();
            return instance;
        }
        else
            return instance;
    }

   
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    
}
