/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.codename1.uikit.cleanmodern;

import com.codename1.components.FloatingHint;
import com.codename1.components.ImageViewer;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.ShareButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.mycompany.Entite.Club;
import com.mycompany.Entite.Event;
import com.mycompany.Entite.User;
import com.mycompany.Service.ClubService;
import com.mycompany.Service.EventService;
import com.mycompany.Service.UserService;
import com.mycompany.utils.Session;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * The user profile form
 *
 * @author Shai Almog
 */
public class ClubDetailsForm extends BaseForm {

    private Image imag;
    private EncodedImage enc;
    private ClubService ser = new ClubService();
    Container ct = new Container();
    Container ect = new Container();
    private ArrayList<Event> events = new ArrayList<Event>();
    EventService eser = new EventService();
    private Resources resource;
    private Club clubb;
    
    public ClubDetailsForm(Resources res, Club club) {
        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        resource = res;
        this.clubb = club;
        String url = "http://localhost/ecolewamp/default.png";
        if(club!= null) url = "http://localhost/ecolewamp/"+club.getLogo();

        try 
        {
           enc = EncodedImage.create("/tennis_club.png");
        } 
        catch (Exception ex) 
        {
            System.err.println(ex);
        }
        
        imag = URLImage.createToStorage(enc, url, url, URLImage.RESIZE_SCALE);                  
        
        
        drawHeader(imag, club);
        add(ct);
        
        ShareButton sb = new ShareButton();
        sb.setText("Share this Event");
        add(sb);
        
        Image screenshot = Image.createImage(getWidth(), getHeight());
        revalidate();
        setVisible(true);
        paintComponent(screenshot.getGraphics(), true);

        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        try(OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch(IOException err) {
            Log.e(err);
        }
        sb.setImageToShare(imageFile, "image/png");

        Button confirm = new Button("Join Club");
        if(ser.isMember(club.getId())) confirm.setText("Leave Club");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                if(confirm.getText().equals("Join Club"))
                {
                    ser.Membership(club.getId(), "true");
                    confirm.setText("Leave Club");
                }
                else
                {
                    ser.Membership(club.getId(), "false");
                    confirm.setText("Join Club");
                }
                    
            }
        });
        
        Container content = BoxLayout.encloseY(
                confirm
        );
     
        content.setScrollableY(true);
        add(content);
        
        
        Label txt = new Label("Club events : ");
        add(txt);
        
        events = eser.getEvents(club.getId());
        for(Event ev : events)
        {
            int deviceWidth = Display.getInstance().getDisplayWidth();
            Image placeholder = Image.createImage(deviceWidth, deviceWidth);
            EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
            String urll = "http://localhost/ecolewamp/"+ev.getCover();
            placeholder = URLImage.createToStorage(encImage, urll, urll);
            drawEvent(placeholder, ev);
        }
        
        add(ect);
        
        Button add = new Button("Create an event");
        if(ser.isMember(club.getId())) confirm.setText("Leave Club");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                new EventForm(res, club, null).show();
            }
        });
        
        
        Container contentt = BoxLayout.encloseY(
                add
        );
     
        contentt.setScrollableY(true);
        if(club.getModerator() == Session.getInstance().getLoggedInUser().getId()) add(contentt);
        
    }
    
    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }
    
    private void drawHeader(Image img, Club c) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       Container cnt = BorderLayout.west(image);
       TextArea ta = new TextArea(c.getName());
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);

       SpanLabel d = new SpanLabel("Club Description: " + c.getDescription());
       
        cnt.add(BorderLayout.CENTER, 
        BoxLayout.encloseY(
            ta,
            BoxLayout.encloseX(d)
        ));
       ct.add(cnt);
   }
    
    private void drawEvent(Image img, Event e) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
      /* image.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) 
           {
              new ClubDetailsForm(resource,c).show();
           }
       });*/
       Container cnt = BorderLayout.west(image);
       TextArea ta = new TextArea(e.getName());
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);

       SpanLabel d = new SpanLabel("Event Details: " + e.getDescription());
       SpanLabel date = new SpanLabel("Event date: " + e.getDate());
       SpanLabel dur = new SpanLabel("Event duration: " + e.getDuration() + " days");
       SpanLabel place = new SpanLabel("Event place: " + e.getPlace());
       
       Label update = new Label("edit");
       FontImage.setMaterialIcon(update, FontImage.MATERIAL_EDIT);
       update.addPointerPressedListener((evt) -> {
           new EventForm(resource, clubb, e).show();
       });
       
       Label delete = new Label("delete");
       FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
       delete.addPointerPressedListener((evt) -> {
           eser.delete(e.getId());
           new ClubDetailsForm(resource,clubb).show();
       });
       
       if(clubb.getModerator() == Session.getInstance().getLoggedInUser().getId())
       {
            cnt.add(BorderLayout.CENTER, 
            BoxLayout.encloseY(
                ta,
                BoxLayout.encloseX(d),
                date,
                dur,
                place,
                BoxLayout.encloseX(update, delete)
            ));
           ect.add(cnt);
       }
       else
       {
            cnt.add(BorderLayout.CENTER, 
            BoxLayout.encloseY(
                ta,
                date,
                dur,
                place,
                BoxLayout.encloseX(d)
            ));
           ect.add(cnt);
       }
       
   }
    
    
}