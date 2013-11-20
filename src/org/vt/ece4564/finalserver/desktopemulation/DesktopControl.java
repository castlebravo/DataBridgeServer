package org.vt.ece4564.finalserver.desktopemulation;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import edu.vt.ece4564.finalproject.databridge.SensorData;


public class DesktopControl
{
    Robot robot;
    Point mouse;
    
    public DesktopControl(){
        try{
            robot = new Robot();
        }catch(Exception e){
            System.err.println("DesktopControl: " + e.toString());
        }
    }
    
    public double[] getMouseXY(){
      //  int height = GraphicsEnvironment.getLocalGraphicsEnvironment()
      //          .getMaximumWindowBounds().height;
        
        Point mouse_point = MouseInfo.getPointerInfo().getLocation();
        double[] toReturn = {mouse_point.x, mouse_point.y};
        return toReturn;
    }
    
    public void doMouseMovement(SensorData sd){
       //Point mouse_point = MouseInfo.getPointerInfo().getLocation();
       // mouse = new Point(mouse_point.x, height - mouse_point.y);
        
        mouse = MouseInfo.getPointerInfo().getLocation();
        
        //size of the screen
       ///// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //height of the task bar
       // Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(MouseInfo.getPointerInfo().getDevice().getDefaultConfiguration());
        //int taskBarSize = scnMax.bottom;
        
     /////  // int height = screenSize.height;
        
        
        
        try{
            robot = new Robot(MouseInfo.getPointerInfo().getDevice());
        }catch(Exception e){
            System.err.println("DesktopControl: " + e.toString());
        }
        
        //MouseInfo.getPointerInfo().
        
        
        // center 2150, 915
        int x = 660;
        int y = 400;
        int delta_x = 0;
        int delta_y = 0;
        
        if(sd.getAcl_y() >= 2){
            y = 200;
            delta_y = -(1 + (int)sd.getAcl_y());
            System.out.print("up, ");
        }
        else if(sd.getAcl_y() <= -2){
            y = 600;
            delta_y = -((int)sd.getAcl_y() - 1);
            System.out.print("down, ");
        }else{
            y = 400;
            System.out.print("none, ");
        }
        
        if(sd.getAcl_x() >= 2){
            x = 460;
            delta_x = -(1 + (int)sd.getAcl_x());
            System.out.print("left, ");
        }
        else if(sd.getAcl_x() <= -2){
            x = 860;
            delta_x = -((int)sd.getAcl_x() - 1);
            System.out.print("right, ");
        }else{
            x = 660;
            System.out.print("none, ");
        }
        
        System.out.println((mouse.x) + "     " + (mouse.y));
        //robot.mouseMove((x), (y));
        
        //400, 400  ==  400, 650
        //400, 400  ==  400, 550
        //
        //robot.mouseMove(400, 0);
        x = mouse.x + delta_x;
        y = (mouse.y) + delta_y - 250;
        robot.mouseMove(x, y);
       // System.out.println((x) + "     " + (y) + "  height: " + height);
      //  robot.mouseMove(2519, 324);
    }
}
