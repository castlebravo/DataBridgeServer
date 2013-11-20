package org.vt.ece4564.finalserver.desktopemulation;

import org.vt.ece4564.finalserver.*;
import java.util.ArrayList;


public class UDPListener
{
    private ArrayList<Thread> threads_;
    public static int port_;
    private DataProcessor data_processor;
    
    
    public UDPListener(int port,  DataProcessor data_processor){
        this.data_processor = data_processor;
        threads_ = new ArrayList<Thread>();
        port_ = port;
    }
    
    
    // starts the UDP listener
    public void openUDPPort(){
        threads_.add(new Thread(new UDPRunnable(port_, data_processor)));
        threads_.get(threads_.size()-1).start();
    }
}
