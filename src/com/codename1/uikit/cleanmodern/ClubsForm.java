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

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
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
import com.codename1.ui.util.Resources;
import com.mycompany.Entite.Club;
import com.mycompany.Service.ClubService;
import com.mycompany.utils.Session;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class ClubsForm extends BaseForm {
    
    private ArrayList<Club> clubs = new ArrayList<Club>();
    private ClubService ser = new ClubService();
    private Resources resource;
    Container ct = new Container();

    public ClubsForm(Resources res,String tab) {
        super("Forum", BoxLayout.y());
        Dialog ip = new InfiniteProgress().showInifiniteBlocking();
        resource = res;
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
        EncodedImage enc;
        EncodedImage enc1;
        try 
        {
            enc = EncodedImage.create("/school.jpg");
            addTab(swipe, enc, spacer1, "", "", "Discover and get involved in a variety of clubs");
            enc1 = EncodedImage.create("/chess.jpg");
            addTab(swipe, enc1, spacer2, "", "", "Host and participate in club events");
        }
        catch (IOException ex) 
        {
        } 
                
        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
        
        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
        ButtonGroup barGroup = new ButtonGroup();
        RadioButton all = RadioButton.createToggle("All", barGroup);
        all.setUIID("SelectBar");
        RadioButton social = RadioButton.createToggle("Social", barGroup);
        social.setUIID("SelectBar");
        RadioButton music = RadioButton.createToggle("Music", barGroup);
        music.setUIID("SelectBar");
        RadioButton sport = RadioButton.createToggle("Sport", barGroup);
        sport.setUIID("SelectBar");
        
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new ClubsForm(resource,"all").show();
            }
        });
        
        social.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new ClubsForm(resource,"social").show();
            }
        });
        
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new ClubsForm(resource,"music").show();
            }
        });
        
        sport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new ClubsForm(resource,"sport").show();
            }
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(4, all, social, music, sport),
                FlowLayout.encloseBottom(arrow)
        ));
        
        all.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(all, arrow);
        });
        bindButtonSelection(all, arrow);
        bindButtonSelection(social, arrow);
        bindButtonSelection(music, arrow);
        bindButtonSelection(sport, arrow);

        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });
            
        if(!tab.equals("all"))
        {
            clubs.clear();
            if(tab.equals("social"))
            {
                social.setSelected(true);
                arrow.setVisible(false);
                addShowListener(e -> {
                    arrow.setVisible(true);
                    updateArrowPosition(social, arrow);
                });
                bindButtonSelection(all, arrow);
                bindButtonSelection(social, arrow);
                bindButtonSelection(music, arrow);
                bindButtonSelection(sport, arrow);
                clubs = ser.getSocialClubs();
            }
            else if(tab.equals("music"))
            {
                music.setSelected(true);
                arrow.setVisible(false);
                addShowListener(e -> {
                    arrow.setVisible(true);
                    updateArrowPosition(music, arrow);
                });
                bindButtonSelection(all, arrow);
                bindButtonSelection(social, arrow);
                bindButtonSelection(music, arrow);
                bindButtonSelection(sport, arrow);
                clubs = ser.getMusicClubs();
            }
            else if(tab.equals("sport"))
            {
                sport.setSelected(true);
                arrow.setVisible(false);
                addShowListener(e -> {
                    arrow.setVisible(true);
                    updateArrowPosition(sport, arrow);
                });
                bindButtonSelection(all, arrow);
                bindButtonSelection(social, arrow);
                bindButtonSelection(music, arrow);
                bindButtonSelection(sport, arrow);
                clubs = ser.getSportClubs();
            }
        
        }
        else
            clubs = ser.getClubs();
        
        for(Club c : clubs)
        {
            int deviceWidth = Display.getInstance().getDisplayWidth();
            Image placeholder = Image.createImage(deviceWidth, deviceWidth);
            EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
            String url = "http://localhost/ecolewamp/"+c.getLogo();
            placeholder = URLImage.createToStorage(encImage, url, url);
            addClubButton(placeholder, c);
        }
        
        add(ct);
        Button create = new Button("Create a new club");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
               new ClubForm(resource, null).show();
            }
        });
        Container content = BoxLayout.encloseY(
                create
        );
     
        content.setScrollableY(true);
        add(content);
    }
    
    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();  
    }
    
    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        
        Container page1 = 
            LayeredLayout.encloseIn(
                image,
                overlay,
                BorderLayout.south(
                    BoxLayout.encloseY(
                            new SpanLabel(text, "LargeWhiteText"),
                            spacer
                        )
                )
            );

        swipe.addTab("", page1);
    }
   
    private void addClubButton(Image img, Club c) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       image.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) 
           {
              new ClubDetailsForm(resource,c).show();
           }
       });
       Container cnt = BorderLayout.west(image);
       TextArea ta = new TextArea(c.getName());
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);


       Label update = new Label("edit");
       FontImage.setMaterialIcon(update, FontImage.MATERIAL_EDIT);
       update.addPointerPressedListener((evt) -> {
           new ClubForm(resource, c).show();
       });
       
       Label delete = new Label("delete");
       FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
       delete.addPointerPressedListener((evt) -> {
           ser.delete(c.getId());
           new ClubsForm(resource,"all").show();
       });
       
       if(c.getModerator() == Session.getInstance().getLoggedInUser().getId())
       {
           cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta,
                       BoxLayout.encloseX(update, delete)
               ));
       }
       else
       {
           cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta
               ));
       }
       
       
       ct.add(cnt);
       image.addActionListener(e ->  new ClubDetailsForm(resource,c).show());
   }
    
    private void bindButtonSelection(Button b, Label arrow) {
        b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
            }
        });
    }
}
