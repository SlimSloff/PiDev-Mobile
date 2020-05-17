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
import com.mycompany.Entite.Event;
import com.mycompany.utils.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kinto
 */
public class EventService {
    public ArrayList<Event> parseEventJson(String json) throws ParseException {

        ArrayList<Event> listPosts = new ArrayList<>();

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
                Event e = new Event();
                
                float id = Float.parseFloat(obj.get("id").toString());
                float clubid = Float.parseFloat(obj.get("clubId").toString());
                float duration = Float.parseFloat(obj.get("eventduration").toString());

                e.setId((int)id);
                e.setClub_id((int)clubid);
                e.setName(obj.get("eventname").toString());
                e.setDescription(obj.get("eventdetails").toString());
                e.setDate(obj.get("eventdate").toString());
                e.setDuration((int) duration);
                e.setPlace(obj.get("eventplace").toString());
                e.setCover(obj.get("cover").toString());
                listPosts.add(e);

            }

        } catch (IOException ex) {
        }
        
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
        */
        return listPosts;
    }
    
    ArrayList<Event> listEvents = new ArrayList<>();
    
    public ArrayList<Event> getEvents(int clubid){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/School/web/app_dev.php/events/" + clubid);  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                EventService ser = new EventService();
                try {
                    listEvents = ser.parseEventJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listEvents;
    }
    
    public void add(Event e) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/addEvent?name="+e.getName()+"&description="+e.getDescription()+"&duration="+e.getDuration()+"&place="+e.getPlace()+
                "&date="+e.getDate()+"&club_id="+e.getClub_id();
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((ev) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
 
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    
    public void update(Event e) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/update_event?name="+e.getName()+"&description="+e.getDescription()+"&duration="+e.getDuration()+"&place="+e.getPlace()+
                "&date="+e.getDate()+"&id="+e.getId();
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((ev) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
 
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    
    public void delete(int id) 
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost/School/web/app_dev.php/delete_event/" + id;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
       
        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
  
}
