package org.vt.ece4564.finalserver;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.vt.ece4564.finalserver.chat.Chat;
import org.vt.ece4564.finalserver.cli.CLI;
import org.vt.ece4564.finalserver.desktopemulation.DataProcessor;
import org.vt.ece4564.finalserver.desktopemulation.DataUpdateServlet;
import org.vt.ece4564.finalserver.desktopemulation.DesktopControl;
import org.vt.ece4564.finalserver.desktopemulation.UDPListener;


// configures the server and starts it
public class ServerInitializer
{
    private DataProcessor data_processor;
    private DesktopControl desktop_control;
    private static Server server_;
    private ServletContextHandler servletContextHandler;
    public static ServerLog log_;
    public ArrayList<String> server_ip_address;
    private int TCP_P;
    private int UDP_P;
    private String[] interfaces = {"wlan", "wireless", "Wireless", "wifi", "Wifi"};
    private JTextArea home_text_area;
    
    
    public ServerInitializer(JTextArea a, JTextArea message_log, JTable CtrlDebugTable){
        home_text_area = a;
        
        server_ip_address = new ArrayList<String>();
        
        TCP_P = getTCPPort();
        UDP_P = getUDPPort();
        
        home_text_area.append("Welcome to the DataBridge Server!\n\n\nTo connect " +
                "to this server, you will need to enter your IP address, TCP port, "
                + "and UDP port into the companion android application. It may "
                + "be possible to obtain some of this information programatically. "
                + "The results of this search follow:\n\n");
        try{    // loops through all the network interfaces on the machine
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()){
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
 
                if(isCorrect(n.getDisplayName())){ 
                    while(ee.hasMoreElements()){
                        InetAddress i = (InetAddress)ee.nextElement();
                        if(i.getHostAddress().contains(".")){
                            server_ip_address.add(i.getHostAddress());
                        }
                    }
                }
            }
        }catch(Exception e){
            System.err.println("ERROR Failed to get default connection " +
                "information: " + e.toString());
        }
        
        // display connection information
        for(int i = 0; i < server_ip_address.size(); ++i){
            home_text_area.append("IP Address: " + server_ip_address.get(i) + "\n");
        }
        
        home_text_area.append("TCP Port: " + Integer.toString(TCP_P) + "\n");
        home_text_area.append("UDP Port: " + Integer.toString(UDP_P) + "\n");
        
       
        // UDP security
        String Authentication = generateAuthenticationCode();

        home_text_area.append("\n\n\nIn addition to the above, you will "
                + "need to enter the following authentication code:\n\n"
                + "Authentication code: " + Authentication + "\n");
        
        // initialize the log object
        log_ = new ServerLog(message_log);
        
        desktop_control = new DesktopControl(Authentication);
        data_processor = new DataProcessor(CtrlDebugTable, desktop_control);
        
        // start the server
        startServer();        
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    private void startServer(){
        new UDPListener(UDP_P, data_processor).openUDPPort();
         
        server_ = new Server(TCP_P);
        
        // Create servlet context handler for main servlet.
        servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        
        // add static content
        addStaticContent("resources/index.htm", "/*");
        addStaticContent("resources/cli.htm", "/cli.htm");
        addStaticContent("resources/post.htm", "/post.htm");
        addStaticContent("resources/chat.htm", "/chat.htm");

        // add servlets
        servletContextHandler.addServlet(new ServletHolder(new DataUpdateServlet(desktop_control)), "/dataupdateterminal");
        servletContextHandler.addServlet(new ServletHolder(new CLI()), "/cli");
        servletContextHandler.addServlet(new ServletHolder(new Chat()), "/chat");

        server_.setHandler(servletContextHandler);
 
        try{   
            server_.start();
        }catch(Exception e){
            System.err.println(e.toString());
        }
    }
    
    
    private String generateAuthenticationCode(){
        String Authentication = 
                Integer.toString(1111 + (int)(Math.random()*((999999999 - 1111) + 1)));
        
        try{
            byte[] bytesOfMessage = 
                    (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                    .format(new Date())).getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            byte[] authentication = new byte[thedigest.length];
            int auth_index = 0;
            
            for(int i = 0; i < thedigest.length; ++i){
                //                 33                    126
                if(thedigest[i] >= 48 && thedigest[i] <= 122){
                    authentication[auth_index] = thedigest[i];
                    auth_index++;
                }
            }
            Authentication = new String(authentication).trim();
            
        }catch(Exception e){
            System.err.println(e.toString());
        }
        return Authentication;
    }
    
    private int getTCPPort(){
        for(int i = 8080; i < 10000; ++i){
            if(portOpenTCP(i)){
                return i;
            }
        }  
        return 0;
    }
    
    private int getUDPPort(){
        for(int i = 1000; i < 5000; ++i){
            if(portOpenUDP(i)){
                return i;
            }
        }
        return 0;
    }
    
    
    private boolean isCorrect(String s){
        for(int i = 0; i < interfaces.length; ++i){
            if(s.contains(interfaces[i])){
                return true;
            }
        }
        return false;
    }
    
    private boolean portOpenUDP(int port){
        DatagramSocket d = null;
        try {
            d = new DatagramSocket(port);
            d.setReuseAddress(true);
            return true;
        }catch(IOException e){}
        finally{
            if(d != null)
                d.close();
        }
        return false;
    }
    
    private boolean portOpenTCP(int port){
        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
            s.setReuseAddress(true);
            return true;
        }catch(IOException e){}
        finally{
            if(s != null){
                try {
                    s.close();
                }catch(IOException e){}
            }
        }
        return false;
    }
    
    private void addStaticContent(String resource, String mapping){
        servletContextHandler.addServlet(new ServletHolder(new StaticContentServlet(resource)), mapping);
    }
}
