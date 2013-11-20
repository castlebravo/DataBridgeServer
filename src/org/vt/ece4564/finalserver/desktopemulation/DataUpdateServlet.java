package org.vt.ece4564.finalserver.desktopemulation;

import org.vt.ece4564.finalserver.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class DataUpdateServlet extends HttpServlet
{   
    private DataProcessor data_processor;
    
    
    public DataUpdateServlet(DataProcessor data_processor){
        this.data_processor = data_processor;
    }
    
    
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        // if the HTTP GET contained a query
        if(req.getQueryString() != null){
            
            // if it is the Google Chart data query
            if(req.getQueryString().contains("tq=chart"))
                resp.getWriter().write(data_processor.getFormattedData());  

            
            // if it is the mobile app requesting the UDP port
            else if(req.getQueryString().contains("req_udp_port"))
                resp.getWriter().write(Integer.toString(UDPListener.port_));

            
            // if it is the mobile app requesting the sensor data to be cleared
            else if(req.getQueryString().contains("req_data_clr"))
                data_processor.clearData();
            
        }
    }  
}