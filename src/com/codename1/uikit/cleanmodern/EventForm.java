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
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
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
import com.codename1.ui.util.Resources;
import com.mycompany.Entite.Club;
import com.mycompany.Entite.Event;
import com.mycompany.Entite.User;
import com.mycompany.Service.ClubService;
import com.mycompany.Service.EventService;
import com.mycompany.Service.UserService;
import com.mycompany.utils.Session;
import java.util.Date;

/**
 * The user profile form
 *
 * @author Shai Almog
 */
public class EventForm extends BaseForm {

    String[] characters = { "Social", "Musique", "Sport"};
    private Image imag;
    private EncodedImage enc;
    private EventService ser = new EventService();
    private int ev_id;
    
    public EventForm(Resources res, Club club, Event event) {
        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        getContentPane().setScrollVisible(false);
        if(event != null) ev_id = event.getId();
        super.addSideMenu(res);
       
        String url = "http://localhost/ecolewamp/event.png";
        if(event!= null) url = "http://localhost/ecolewamp/"+event.getCover();

        try 
        {
           enc = EncodedImage.create("/event.png");
        } 
        catch (Exception ex) 
        {
            System.err.println(ex);
        }
        
        imag = URLImage.createToStorage(enc, url, url, URLImage.RESIZE_SCALE);                  
        ImageViewer img = new ImageViewer(imag);
        
        add(img);
        
        TextField name = new TextField();
        if(event!= null) name.setText(event.getName());
        name.setUIID("TextFieldBlack");
        addStringValue("Event Name", name);

        TextField description = new TextField();
        if(event!= null) description.setText(event.getDescription());
        description.setUIID("TextFieldBlack");
        addStringValue("Event Description", description);
        
        TextField duration = new TextField();
        if(event!= null) duration.setText(String.valueOf(event.getDuration()));
        duration.setUIID("TextFieldBlack");
        addStringValue("Event duration", duration);
        
        TextField place = new TextField();
        if(event!= null) place.setText(event.getPlace());
        place.setUIID("TextFieldBlack");
        addStringValue("Event place", place);
        
       Picker datePicker = new Picker();
       datePicker.setType(Display.PICKER_TYPE_DATE);
       datePicker.setUIID("TextFieldBlack");
       datePicker.setDate(new Date());
       add(datePicker);
       
        Button confirm = new Button("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                if(event != null)
                {
                    Event event = new Event(club.getId(), name.getText(), description.getText(), datePicker.getValue().toString(), Integer.valueOf(duration.getText()), place.getText(), "cover");
                    event.setId(ev_id);
                    ser.update(event);
                    new ClubDetailsForm(res,club).show();
                }
                else{
                    Event event = new Event(club.getId(), name.getText(), description.getText(), datePicker.getValue().toString(), Integer.valueOf(duration.getText()), place.getText(), "cover");
                    ser.add(event);
                    new ClubDetailsForm(res,club).show();
                }
                    
            }
        });
        Container content = BoxLayout.encloseY(
                confirm
        );
     
        content.setScrollableY(true);
        add(content);
    }
    
    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }
}