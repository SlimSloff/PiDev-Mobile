/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Service;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.ui.events.ActionListener;
import com.mycompany.Entite.Club;
import com.mycompany.utils.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kinto
 */
public class ClubService {
    public ArrayList<Club> parseClubJson(String json) throws ParseException {

        ArrayList<Club> listPosts = new ArrayList<>();

        try {
            JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

            /*
                On doit convertir notre réponse texte en CharArray à fin de
            permettre au JSONParser de la lire et la manipuler d'ou vient 
            l'utilité de new CharArrayReader(json.toCharArray())
            
            La méthode parse json retourne une MAP<String,Object> ou String est 
            la clé principale de notre résultat.
            Dans notre cas la clé principale n'est pas définie cela ne veux pas
            dire qu'elle est manquante mais plutôt gardée à la valeur par defaut
            qui est root.
            En fait c'est la clé de l'objet qui englobe la totalité des objets 
                    c'est la clé définissant le tableau de tâches.
            */
            Map<String, Object> posts = j.parseJSON(new CharArrayReader(json.toCharArray()));
                       
            
            /* Ici on récupère l'objet contenant notre liste dans une liste 
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche                
            */
            List<Map<String, Object>> list = (List<Map<String, Object>>) posts.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Club c = new Club();
                
                float id = Float.parseFloat(obj.get("id").toString());
                float moderator = Float.parseFloat(obj.get("moderator").toString());
                c.setId((int)id);
                c.setModerator((int)moderator);
                c.setName(obj.get("clubname").toString());
                c.setLogo(obj.get("clublogo").toString());
                c.setDescription(obj.get("clubdescription").toString());
                c.setType(obj.get("clubtype").toString());

                listPosts.add(c);

            }

        } catch (IOException ex) {
        }
        
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
        */
        return listPosts;
    }
    
    ArrayList<Club> listClubs = new ArrayList<>();
    
    public ArrayList<Club> getClubs(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/School/web/app_dev.php/clubs");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ClubService ser = new ClubService();
                try {
                    listClubs = ser.parseClubJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listClubs;
    }
    
    public ArrayList<Club> getSocialClubs(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/School/web/app_dev.php/social");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ClubService ser = new ClubService();
                try {
                    listClubs = ser.parseClubJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listClubs;
    }
    
    public ArrayList<Club> getSportClubs(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/School/web/app_dev.php/sport");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ClubService ser = new ClubService();
                try {
                    listClubs = ser.parseClubJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listClubs;
    }
    
    public ArrayList<Club> getMusicClubs(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/School/web/app_dev.php/music");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ClubService ser = new ClubService();
                try {
                    listClubs = ser.parseClubJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listClubs;
    }
    
    public void add(String name, String description, String type) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/addClub/" + name+"/" + description + "/" + type + "/" + Session.getInstance().getLoggedInUser().getId();// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
 
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    
    public void update(int id, String name, String description, String type) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/updateClub/" + id + "/" + name+"/" + description + "/" + type;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
 
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    
    public void delete(int id) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/delete_club/" + id;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
       
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    
    private boolean result;

    public boolean isMember(int club_id) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/member/" + Session.getInstance().getLoggedInUser().getId() + "/" + club_id;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
        if(str.equals("true"))
        {
            result = true;
        }
        else
        {
           result = false;
        }
        
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
        return result; 
    }
  
    public void Membership(int club_id, String join) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/membership/" + Session.getInstance().getLoggedInUser().getId()+"/" + club_id + "/" + join;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
 
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
}
