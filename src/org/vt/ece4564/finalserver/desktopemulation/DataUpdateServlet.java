package org.vt.ece4564.finalserver.desktopemulation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// receives click requests from the mobile app
public class DataUpdateServlet extends HttpServlet
{   
    private DesktopControl desktop_control;
    
    
    public DataUpdateServlet(DesktopControl dc){
        this.desktop_control = dc;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        // if the HTTP GET contained a query
        if(req.getQueryString() != null){
            
            // if it is the mobile app requesting a left click
            if(req.getQueryString().contains("req_click_left")){
                desktop_control.doLeftClick();
            }
            
            // if it is the mobile app requesting a right click
            else if(req.getQueryString().contains("req_click_right")){
                desktop_control.doRightClick();
            }
        }
    }  
}