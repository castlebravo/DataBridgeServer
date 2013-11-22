package org.vt.ece4564.finalserver.desktopemulation;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import edu.vt.ece4564.finalproject.databridge.SensorData;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.ArrayList;


public class DesktopControl
{
    ScreenManager sm;
    
    public DesktopControl(){
        sm = new ScreenManager();
    }
    
    public double[] getMouseXY(){
        Point mouse_point = MouseInfo.getPointerInfo().getLocation();
        double[] toReturn = {mouse_point.x, mouse_point.y};
        return toReturn;
    }
    
    public void doMouseMovement(SensorData sd){
        int delta_x = 0;
        int delta_y = 0;
        
        if(sd.getAcl_y() >= 2){
            delta_y = -(1 + (int)sd.getAcl_y());
        }
        else if(sd.getAcl_y() <= -2){
            delta_y = -((int)sd.getAcl_y() - 1);
        }
        
        if(sd.getAcl_x() >= 2){
            delta_x = -(1 + (int)sd.getAcl_x());
        }
        else if(sd.getAcl_x() <= -2){
            delta_x = -((int)sd.getAcl_x() - 1);
        }
        sm.moveMouse(delta_x, delta_y);
    }
}



class ScreenManager
{
    public int MAX_HEIGHT;
    public int MAX_WIDTH;
    private ArrayList<Screen> screens;
    
    
    public ScreenManager(){
        screens = new ArrayList<Screen>();
        
        GraphicsDevice[] devices = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        
        for(int i = 0; i < devices.length; i++){
            try{
                screens.add(new Screen(devices[i].getDisplayMode().getWidth(),
                    devices[i].getDisplayMode().getHeight(),
                    devices[i].getIDstring(), new Robot(devices[i])));
            }catch(Exception e){
                System.err.println("ScreenManager: " + e.toString());            
            }
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        MAX_HEIGHT = screenSize.height;
        MAX_WIDTH = screenSize.width;
    }
  
    
    public void moveMouse(int delta_x, int delta_y){
        Screen s = getScreen(MouseInfo.getPointerInfo().getDevice().getIDstring());
        
        if(s != null){
            Point p = MouseInfo.getPointerInfo().getLocation();

            int offset_y = 0;
            int offset_x = 0;
            
            if(MAX_HEIGHT > s.HEIGHT)
                offset_y = MAX_HEIGHT - s.HEIGHT;
            
            if(p.x > (MAX_WIDTH - s.WIDTH))
                offset_x = (MAX_WIDTH - s.WIDTH);
            
            s.ROBOT.mouseMove((p.x + delta_x - offset_x), (p.y + delta_y - offset_y));
            
        }else{
            System.err.println("ScreenManager: GraphicsDevice is null");
        }
    }
    
    private Screen getScreen(String id){
        for(int i = 0; i < screens.size(); i++){
            if(screens.get(i).ID.equals(id)){
                return screens.get(i);
            }
        }
        return null;
    }
}


class Screen
{
    public int WIDTH;
    public int HEIGHT;
    public String ID;
    public Robot ROBOT;
    
    public Screen(int w, int h, String id, Robot r){
        ROBOT = r;
        WIDTH = w;
        HEIGHT = h;
        ID = id;
    }
    
    public void print(){
        System.out.println(ID);
        System.out.println("Width:" + WIDTH);
        System.out.println("Height:" + HEIGHT);
    }
}
